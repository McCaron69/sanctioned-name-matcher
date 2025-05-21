package dev.ivushkin.sanctioned_name_matcher.matcher;

import dev.ivushkin.sanctioned_name_matcher.config.MatcherProperties;
import dev.ivushkin.sanctioned_name_matcher.entity.SanctionedName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ApacheNameMatcherTest {

    private ApacheNameMatcher matcher;

    @BeforeEach
    void setUp() {
        MatcherProperties props = new MatcherProperties();
        var weights = new MatcherProperties.Weight();
        weights.setLevenshtein(0.3);
        weights.setJaro(0.3);
        weights.setJaccard(0.4);
        props.setThreshold(0.85);
        props.setWeight(weights);

        matcher = new ApacheNameMatcher(props);
    }

    @Test
    void shouldReturnEmptyIfNoCandidates() {
        Optional<NameMatcher.MatchResult> result = matcher.match("some name", List.of());
        assertThat(result).isEmpty();
    }

    @Test
    void shouldMatchWhenAboveThreshold() {
        var name = new SanctionedName();
        name.setName("Osama Bin Laden");
        name.setNormalizedName("osama bin laden");

        var result = matcher.match("osama bin laden", List.of(name));

        assertThat(result).isPresent();
        assertThat(result.get().matchedName()).isEqualTo("Osama Bin Laden");
        assertThat(result.get().score()).isGreaterThanOrEqualTo(0.85);
    }

    @Test
    void shouldNotMatchWhenBelowThreshold() {
        var name = new SanctionedName();
        name.setName("Unrelated Name");
        name.setNormalizedName("unrelated name");

        var result = matcher.match("osama bin laden", List.of(name));

        assertThat(result).isEmpty();
    }

    @Test
    void shouldRoundScoreToThreeDecimalPlaces() {
        var name = new SanctionedName();
        name.setName("Near Match");
        name.setNormalizedName("osama bin ladn");

        var result = matcher.match("osama bin laden", List.of(name));

        result.ifPresent(r -> {
            double raw = r.score();
            assertThat(String.valueOf(raw).split("\\.")[1].length()).isLessThanOrEqualTo(3);
        });
    }
}