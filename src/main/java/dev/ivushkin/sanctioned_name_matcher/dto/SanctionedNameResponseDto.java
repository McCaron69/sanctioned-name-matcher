package dev.ivushkin.sanctioned_name_matcher.dto;

public record SanctionedNameResponseDto(
        Long id,
        String sanctionedName,
        String normalizedName
) {}