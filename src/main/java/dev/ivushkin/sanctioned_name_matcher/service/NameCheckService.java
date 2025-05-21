package dev.ivushkin.sanctioned_name_matcher.service;

import dev.ivushkin.sanctioned_name_matcher.dto.MatchResultDto;
import dev.ivushkin.sanctioned_name_matcher.entity.SanctionedName;
import dev.ivushkin.sanctioned_name_matcher.exception.EmptyNormalizedNameException;
import dev.ivushkin.sanctioned_name_matcher.matcher.NameMatcher;
import dev.ivushkin.sanctioned_name_matcher.repository.SanctionedNameRepository;
import dev.ivushkin.sanctioned_name_matcher.util.TextNormalizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NameCheckService {

    private final SanctionedNameRepository sanctionedNameRepository;
    private final NameMatcher nameMatcher;

    public NameCheckService(SanctionedNameRepository sanctionedNameRepository, NameMatcher nameMatcher) {
        this.sanctionedNameRepository = sanctionedNameRepository;
        this.nameMatcher = nameMatcher;
    }

    /**
     * Checks whether the given name matches any sanctioned name.
     *
     * <p>Delegates the actual matching logic to the configured {@link NameMatcher} implementation.</p>
     *
     * @param inputName Input name to check
     * @return detailed result of the matching operation
     * @throws EmptyNormalizedNameException if the normalized input name is empty
     */
    public MatchResultDto check(String inputName) {
        log.info("Starting check for name '{}'", inputName);

        String normalized = TextNormalizer.normalize(inputName);
        log.debug("Normalized input '{}'", normalized);

        if (normalized.isBlank()) {
            throw new EmptyNormalizedNameException(inputName);
        }

        List<SanctionedName> sanctionedNames = sanctionedNameRepository.findAll();
        log.debug("Found {} sanctioned names from database", sanctionedNames.size());

        return nameMatcher.match(normalized, sanctionedNames)
                .map(match -> {
                    log.info("Match found: '{}', score: {}", match.matchedName(), match.score());
                    return new MatchResultDto(
                            true,
                            inputName,
                            normalized,
                            match.matchedName(),
                            match.score()
                    );
                })
                .orElseGet(() -> {
                    log.info("No match found for input '{}'", inputName);
                    return new MatchResultDto(false, inputName, normalized, null, 0.0);
                });
    }
}
