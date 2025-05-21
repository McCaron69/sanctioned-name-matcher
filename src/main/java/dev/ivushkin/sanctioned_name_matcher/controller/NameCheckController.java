package dev.ivushkin.sanctioned_name_matcher.controller;

import dev.ivushkin.sanctioned_name_matcher.dto.MatchResultDto;
import dev.ivushkin.sanctioned_name_matcher.dto.NameCheckRequestDto;
import dev.ivushkin.sanctioned_name_matcher.service.NameCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/check")
@Tag(name = "Name Check", description = "Check input names against the sanctioned list")
public class NameCheckController {

    private final NameCheckService nameCheckService;

    public NameCheckController(NameCheckService nameCheckService) {
        this.nameCheckService = nameCheckService;
    }

    @PostMapping
    @Operation(
            summary = "Check name",
            description = "Returns a match result by comparing the input name with all sanctioned names"
    )
    @ApiResponse(responseCode = "200", description = "Match result returned")
    public ResponseEntity<MatchResultDto> checkName(@RequestBody @Valid NameCheckRequestDto request) {
        var result = nameCheckService.check(request.name());
        return ResponseEntity.ok(result);
    }
}
