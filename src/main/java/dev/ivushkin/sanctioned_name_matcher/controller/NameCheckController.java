package dev.ivushkin.sanctioned_name_matcher.controller;

import dev.ivushkin.sanctioned_name_matcher.dto.MatchResultDto;
import dev.ivushkin.sanctioned_name_matcher.dto.NameCheckRequestDto;
import dev.ivushkin.sanctioned_name_matcher.service.NameCheckService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/check")
public class NameCheckController {

    private final NameCheckService nameCheckService;

    public NameCheckController(NameCheckService nameCheckService) {
        this.nameCheckService = nameCheckService;
    }

    @PostMapping
    public ResponseEntity<MatchResultDto> checkName(@RequestBody @Valid NameCheckRequestDto request) {
        var result = nameCheckService.check(request.name());
        return ResponseEntity.ok(result);
    }
}
