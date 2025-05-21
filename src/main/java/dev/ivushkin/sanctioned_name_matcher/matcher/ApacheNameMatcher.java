package dev.ivushkin.sanctioned_name_matcher.matcher;

import dev.ivushkin.sanctioned_name_matcher.config.MatcherProperties;
import dev.ivushkin.sanctioned_name_matcher.entity.SanctionedName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Name matcher based on fuzzy string similarity using Apache Commons Text algorithms.
 *
 * <p>Uses a weighted combination of Levenshtein, Jaro-Winkler and Jaccard similarity metrics.</p>
 */
@Slf4j
@Component
public class ApacheNameMatcher implements NameMatcher {

    private final MatcherProperties properties;

    private static final LevenshteinDistance LEVENSHTEIN = LevenshteinDistance.getDefaultInstance();
    private static final JaroWinklerSimilarity JARO = new JaroWinklerSimilarity();
    private static final JaccardSimilarity JACCARD = new JaccardSimilarity();

    public ApacheNameMatcher(MatcherProperties properties) {
        this.properties = properties;
    }

    /**
     * Attempts to find the best matching name from the list of candidates.
     *
     * @param normalizedInput normalized input name
     * @param candidates list of sanctioned names (with normalized values)
     * @return best match with score, or empty if no match passes threshold
     */
    @Override
    public Optional<MatchResult> match(String normalizedInput, List<SanctionedName> candidates) {
        if (candidates.isEmpty()) {
            return Optional.empty();
        }

        return candidates.stream()
                .map(candidate -> new CandidateScore(candidate, getCombinedScore(normalizedInput, candidate)))
                .filter(candidateScore -> isAboveThreshold(candidateScore.score()))
                .max(Comparator.comparingDouble(CandidateScore::score))
                .map(best -> new MatchResult(best.name().getName(), roundScore(best.score())));
    }

    private record CandidateScore(SanctionedName name, double score) {}

    private boolean isAboveThreshold(double score) {
        return score >= properties.getThreshold();
    }

    private double getCombinedScore(String normalizedInput, SanctionedName candidate) {
        String normalizedCandidate = candidate.getNormalizedName();

        double levenshteinScore = similarityByLevenshtein(normalizedInput, normalizedCandidate);
        double jaroScore = JARO.apply(normalizedInput, normalizedCandidate);
        double jaccardScore = JACCARD.apply(normalizedInput, normalizedCandidate);

        double combinedScore =
                properties.getWeight().getLevenshtein() * levenshteinScore +
                properties.getWeight().getJaro() * jaroScore +
                properties.getWeight().getJaccard() * jaccardScore;

        log.info(
                "Comparing input '{}' to candidate '{}': levenshtein={}, jaro={}, jaccard={}, score={}",
                normalizedInput,
                normalizedCandidate,
                formatScore(levenshteinScore),
                formatScore(jaroScore),
                formatScore(jaccardScore),
                formatScore(combinedScore)
        );

        return combinedScore;
    }

    private double similarityByLevenshtein(String input, String candidate) {
        int distance = LEVENSHTEIN.apply(input, candidate);
        int maxLength = Math.max(input.length(), candidate.length());
        return maxLength == 0 ? 1.0 : 1.0 - ((double) distance / maxLength);
    }

    private static double roundScore(double score) {
        return Math.round(score * 1000.0) / 1000.0;
    }

    private static String formatScore(double value) {
        return String.format("%.3f", value);
    }
}
