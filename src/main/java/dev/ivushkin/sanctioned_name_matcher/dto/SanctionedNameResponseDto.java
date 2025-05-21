package dev.ivushkin.sanctioned_name_matcher.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sanctioned name with ID and normalized form")
public record SanctionedNameResponseDto(

        @Schema(description = "Unique identifier", example = "1")
        Long id,

        @Schema(description = "Original name of the sanctioned person", example = "Osama Bin Laden")
        String sanctionedName,

        @Schema(description = "Normalized version of the name", example = "bin laden osama")
        String normalizedName
) {}