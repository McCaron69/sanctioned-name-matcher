package dev.ivushkin.sanctioned_name_matcher.service;

import dev.ivushkin.sanctioned_name_matcher.entity.SanctionedName;
import dev.ivushkin.sanctioned_name_matcher.exception.DuplicateSanctionedNameException;
import dev.ivushkin.sanctioned_name_matcher.exception.EmptyNormalizedNameException;
import dev.ivushkin.sanctioned_name_matcher.exception.NormalizedNameTooShortException;
import dev.ivushkin.sanctioned_name_matcher.repository.SanctionedNameRepository;
import dev.ivushkin.sanctioned_name_matcher.util.TextNormalizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SanctionedNameServiceTest {

    @Mock
    private SanctionedNameRepository repository;

    @InjectMocks
    private SanctionedNameService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddNewSanctionedName() {
        String name = "Osama Bin Laden";
        String normalized = TextNormalizer.normalize(name);

        when(repository.existsByNormalizedName(normalized)).thenReturn(false);
        when(repository.save(any())).thenAnswer(invocation -> {
            SanctionedName n = invocation.getArgument(0);
            n.setId(1L);
            return n;
        });

        var result = service.addSanctionedName(name);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getNormalizedName()).isEqualTo(normalized);
    }

    @Test
    void shouldThrowWhenDuplicateName() {
        String name = "Osama Bin Laden";
        String normalized = TextNormalizer.normalize(name);

        when(repository.existsByNormalizedName(normalized)).thenReturn(true);

        assertThatThrownBy(() -> service.addSanctionedName(name))
                .isInstanceOf(DuplicateSanctionedNameException.class);
    }

    @Test
    void shouldThrowWhenNormalizedNameIsEmpty() {

        assertThatThrownBy(() -> service.addSanctionedName("Mr."))
                .isInstanceOf(EmptyNormalizedNameException.class);
    }

    @Test
    void shouldThrowWhenNormalizedNameTooShort() {
        assertThatThrownBy(() -> service.addSanctionedName("Al"))
                .isInstanceOf(NormalizedNameTooShortException.class);
    }

    @Test
    void shouldDeleteById() {
        service.deleteSanctionedName(42L);
        verify(repository).deleteById(42L);
    }

    @Test
    void shouldUpdateSanctionedName() {
        var sanctionedName = new SanctionedName();
        sanctionedName.setId(123L);
        sanctionedName.setName("Old Name");
        sanctionedName.setNormalizedName("old name");

        String newName = "Ali Bin";

        when(repository.findById(123L)).thenReturn(Optional.of(sanctionedName));
        when(repository.existsByNormalizedNameAndIdNot("ali bin", 123L)).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.changeSanctionedName(123L, newName);

        assertThat(result.getName()).isEqualTo(newName);
        assertThat(result.getNormalizedName()).isEqualTo("ali bin");
    }
}
