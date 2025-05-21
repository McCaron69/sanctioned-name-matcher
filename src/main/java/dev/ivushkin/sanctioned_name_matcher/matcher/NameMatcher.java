package dev.ivushkin.sanctioned_name_matcher.matcher;

import dev.ivushkin.sanctioned_name_matcher.entity.SanctionedName;

import java.util.List;
import java.util.Optional;

public interface NameMatcher {
    record MatchResult(String matchedName, double score) {}

    Optional<MatchResult> match(String normalizedName, List<SanctionedName> candidates);
}
