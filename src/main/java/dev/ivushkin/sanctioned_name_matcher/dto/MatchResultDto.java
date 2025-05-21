package dev.ivushkin.sanctioned_name_matcher.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Match result for name checking")
public record MatchResultDto(

        @Schema(description = "Whether a match was found", example = "true")
        boolean match,

        @Schema(description = "Original input name", example = "Osama Laden")
        String inputName,

        @Schema(description = "Normalized version of the input name", example = "laden osama")
        String normalizedInput,

        @Schema(description = "Matched sanctioned name (if any)", example = "Osama Bin Laden")
        String matchedName,

        @Schema(description = "Similarity score between input and matched name", example = "0.914")
        double similarityScore
) {}
