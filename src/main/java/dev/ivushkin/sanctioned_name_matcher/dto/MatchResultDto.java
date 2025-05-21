package dev.ivushkin.sanctioned_name_matcher.dto;

public record MatchResultDto(
        boolean match,
        String inputName,
        String normalizedInput,
        String matchedName,
        double similarityScore
) {}
