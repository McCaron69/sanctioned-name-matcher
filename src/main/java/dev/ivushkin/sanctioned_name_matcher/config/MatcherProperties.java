package dev.ivushkin.sanctioned_name_matcher.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "matcher")
public class MatcherProperties {

    private double threshold;

    private Weight weight = new Weight();

    @Getter
    @Setter
    public static class Weight {
        private double levenshtein;
        private double jaro;
        private double jaccard;
    }
}
