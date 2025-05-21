package dev.ivushkin.sanctioned_name_matcher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request for checking a name")
public record NameCheckRequestDto(

        @Schema(
                description = "Input name to be checked against the sanctioned list",
                example = "Osama Laden",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank String name
) {}
