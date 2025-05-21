package dev.ivushkin.sanctioned_name_matcher.dto;

import jakarta.validation.constraints.NotBlank;

public record NameCheckRequestDto(
        @NotBlank String name
) {}
