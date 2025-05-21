package dev.ivushkin.sanctioned_name_matcher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request for creating or updating a sanctioned name")
public record SanctionedNameRequestDto(

        @Schema(
                description = "The full name of the sanctioned person",
                example = "Osama Bin Laden",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank String name
) {}
