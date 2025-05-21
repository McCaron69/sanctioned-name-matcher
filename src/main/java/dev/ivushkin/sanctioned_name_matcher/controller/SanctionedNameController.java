package dev.ivushkin.sanctioned_name_matcher.controller;

import dev.ivushkin.sanctioned_name_matcher.dto.SanctionedNameRequestDto;
import dev.ivushkin.sanctioned_name_matcher.dto.SanctionedNameResponseDto;
import dev.ivushkin.sanctioned_name_matcher.service.SanctionedNameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sanctions")
@Tag(name = "Sanctioned Names", description = "CRUD operations for managing sanctioned names")
public class SanctionedNameController {

    private final SanctionedNameService sanctionedNameService;

    public SanctionedNameController(SanctionedNameService sanctionedNameService) {
        this.sanctionedNameService = sanctionedNameService;
    }

    @GetMapping
    @Operation(summary = "Get all sanctioned names")
    public List<SanctionedNameResponseDto> getAllSanctionedNames() {
        return sanctionedNameService.getListOfSanctionedNames().stream()
                .map(name -> new SanctionedNameResponseDto(
                        name.getId(),
                        name.getName(),
                        name.getNormalizedName()))
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sanctioned name by ID")
    @ApiResponse(responseCode = "200", description = "Sanctioned name returned")
    public ResponseEntity<SanctionedNameResponseDto> getSanctionedNameById(@PathVariable long id) {
        var sanctionedName = sanctionedNameService.getById(id);
        var nameDto = new SanctionedNameResponseDto(
                sanctionedName.getId(),
                sanctionedName.getName(),
                sanctionedName.getNormalizedName()
        );
        return ResponseEntity.ok(nameDto);
    }

    @PostMapping
    @Operation(summary = "Add new sanctioned name")
    @ApiResponse(responseCode = "200", description = "Sanctioned name added")
    public ResponseEntity<SanctionedNameResponseDto> addSanctionedName(
            @RequestBody @Valid SanctionedNameRequestDto request
    ) {
        var newSanctionedName = sanctionedNameService.addSanctionedName(request.name());
        var nameDto = new SanctionedNameResponseDto(
                newSanctionedName.getId(),
                newSanctionedName.getName(),
                newSanctionedName.getNormalizedName()
        );
        return ResponseEntity.ok(nameDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update sanctioned name")
    @ApiResponse(responseCode = "200", description = "Sanctioned name updated")
    public ResponseEntity<SanctionedNameResponseDto> updateSanctionedName(
            @PathVariable long id,
            @RequestBody @Valid SanctionedNameRequestDto request
    ) {
        var updatedName = sanctionedNameService.changeSanctionedName(id, request.name());
        var nameDto = new SanctionedNameResponseDto(
                updatedName.getId(),
                updatedName.getName(),
                updatedName.getNormalizedName()
        );
        return ResponseEntity.ok(nameDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sanctioned name")
    @ApiResponse(responseCode = "204", description = "Sanctioned name deleted")
    public ResponseEntity<Void> deleteSanctionedName(@PathVariable long id) {
        sanctionedNameService.deleteSanctionedName(id);
        return ResponseEntity.noContent().build();
    }
}
