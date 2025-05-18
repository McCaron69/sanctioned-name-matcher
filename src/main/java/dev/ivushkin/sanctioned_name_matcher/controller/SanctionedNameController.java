package dev.ivushkin.sanctioned_name_matcher.controller;

import dev.ivushkin.sanctioned_name_matcher.dto.SanctionedNameRequestDto;
import dev.ivushkin.sanctioned_name_matcher.dto.SanctionedNameResponseDto;
import dev.ivushkin.sanctioned_name_matcher.service.SanctionedNameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sanctions")
public class SanctionedNameController {

    private final SanctionedNameService sanctionedNameService;

    public SanctionedNameController(SanctionedNameService sanctionedNameService) {
        this.sanctionedNameService = sanctionedNameService;
    }

    @GetMapping
    public List<SanctionedNameResponseDto> getAllSanctionedNames() {
        return sanctionedNameService.getListOfSanctionedNames().stream()
                .map(name -> new SanctionedNameResponseDto(
                        name.getId(),
                        name.getName(),
                        name.getNormalizedName()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanctionedNameResponseDto> getSanctionedNameById(@PathVariable long id) {
        var entity = sanctionedNameService.getById(id);
        var nameDto = new SanctionedNameResponseDto(entity.getId(), entity.getName(), entity.getNormalizedName());
        return ResponseEntity.ok(nameDto);
    }

    @PostMapping
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
    public ResponseEntity<Void> deleteSanctionedName(@PathVariable long id) {
        sanctionedNameService.deleteSanctionedName(id);
        return ResponseEntity.noContent().build();
    }

}
