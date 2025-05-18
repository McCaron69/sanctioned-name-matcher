package dev.ivushkin.sanctioned_name_matcher.repository;

import dev.ivushkin.sanctioned_name_matcher.entity.SanctionedName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanctionedNameRepository extends JpaRepository<SanctionedName, Long> {
    boolean existsByNormalizedName(String normalizedName);
    boolean existsByNormalizedNameAndIdNot(String normalizedName, Long id);
}
