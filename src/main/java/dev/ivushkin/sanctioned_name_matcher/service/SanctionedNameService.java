package dev.ivushkin.sanctioned_name_matcher.service;

import dev.ivushkin.sanctioned_name_matcher.entity.SanctionedName;
import dev.ivushkin.sanctioned_name_matcher.exception.DuplicateSanctionedNameException;
import dev.ivushkin.sanctioned_name_matcher.exception.EmptyNormalizedNameException;
import dev.ivushkin.sanctioned_name_matcher.exception.NormalizedNameTooShortException;
import dev.ivushkin.sanctioned_name_matcher.exception.SanctionedNameNotFoundException;
import dev.ivushkin.sanctioned_name_matcher.repository.SanctionedNameRepository;
import dev.ivushkin.sanctioned_name_matcher.util.TextNormalizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SanctionedNameService {

    private static final int MIN_NORMALIZED_NAME_LENGTH = 3;

    private final SanctionedNameRepository sanctionedNameRepository;

    public SanctionedNameService(SanctionedNameRepository sanctionedNameRepository) {
        this.sanctionedNameRepository = sanctionedNameRepository;
    }

    public List<SanctionedName> getListOfSanctionedNames() {
        return sanctionedNameRepository.findAll();
    }

    public SanctionedName getById(long id) {
        return sanctionedNameRepository.findById(id)
                .orElseThrow(() -> new SanctionedNameNotFoundException(id));
    }

    public SanctionedName addSanctionedName(String name) {
        var normalizedName = normalizeAndValidateName(name, null);

        var sanctionedName = new SanctionedName();
        sanctionedName.setName(name);
        sanctionedName.setNormalizedName(normalizedName);
        var savedName = sanctionedNameRepository.save(sanctionedName);

        log.info("Added sanctioned name [id={}, name='{}', normalized='{}']",
                savedName.getId(), savedName.getName(), savedName.getNormalizedName());

        return savedName;
    }

    public SanctionedName changeSanctionedName(long id, String name) {
        var normalizedName = normalizeAndValidateName(name, id);

        var existingSanctionedName = this.getById(id);
        existingSanctionedName.setName(name);
        existingSanctionedName.setNormalizedName(normalizedName);
        var updatedSanctionedName = sanctionedNameRepository.save(existingSanctionedName);

        log.info("Updated sanctioned name [id={}, name='{}', normalized='{}']",
                id, updatedSanctionedName.getName(), updatedSanctionedName.getNormalizedName());

        return updatedSanctionedName;
    }

    public void deleteSanctionedName(long id) {
        sanctionedNameRepository.deleteById(id);
        log.info("Deleted sanctioned name with id {}", id);
    }

    private String normalizeAndValidateName(String name, Long nameId) {
        var normalizedName = TextNormalizer.normalize(name);

        if (normalizedName.isBlank()) {
            throw new EmptyNormalizedNameException(name);
        }
        if (normalizedName.length() < MIN_NORMALIZED_NAME_LENGTH) {
            throw new NormalizedNameTooShortException(normalizedName, MIN_NORMALIZED_NAME_LENGTH);
        }

        validateNoDuplicates(name, nameId, normalizedName);

        return normalizedName;
    }

    private void validateNoDuplicates(String name, Long nameId, String normalizedName) {
        boolean duplicate = (nameId == null)
                ? sanctionedNameRepository.existsByNormalizedName(normalizedName)
                : sanctionedNameRepository.existsByNormalizedNameAndIdNot(normalizedName, nameId);
        if (duplicate) {
            throw new DuplicateSanctionedNameException(name);
        }
    }

}
