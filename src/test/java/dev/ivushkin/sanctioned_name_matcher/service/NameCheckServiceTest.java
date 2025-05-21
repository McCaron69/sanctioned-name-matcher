package dev.ivushkin.sanctioned_name_matcher.service;

import dev.ivushkin.sanctioned_name_matcher.dto.MatchResultDto;
import dev.ivushkin.sanctioned_name_matcher.entity.SanctionedName;
import dev.ivushkin.sanctioned_name_matcher.exception.EmptyNormalizedNameException;
import dev.ivushkin.sanctioned_name_matcher.matcher.NameMatcher;
import dev.ivushkin.sanctioned_name_matcher.repository.SanctionedNameRepository;
import dev.ivushkin.sanctioned_name_matcher.util.TextNormalizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NameCheckServiceTest {

    private SanctionedNameRepository repository;
    private NameMatcher matcher;
    private NameCheckService service;

    @BeforeEach
    void setUp() {
        repository = mock(SanctionedNameRepository.class);
        matcher = mock(NameMatcher.class);
        service = new NameCheckService(repository, matcher);
    }

    @ParameterizedTest
    @ValueSource(strings = { "mr", "mrs", "ms", "the", "and", "to", "an", "a", "of", "dr", "sir", "miss" })
    void shouldThrowExceptionWhenNameIsOnlyNoiseWords(String input) {
        assertThrows(EmptyNormalizedNameException.class, () -> service.check(input));
    }

    @Test
    void shouldReturnMatchWhenFound() {
        String input = "Osama Bin Laden";
        String normalized = TextNormalizer.normalize(input);

        SanctionedName name = new SanctionedName();
        name.setName("Osama Bin Laden");
        name.setNormalizedName(normalized);

        when(repository.findAll()).thenReturn(List.of(name));
        when(matcher.match(eq(normalized), anyList()))
                .thenReturn(Optional.of(new NameMatcher.MatchResult(name.getName(), 0.91)));

        MatchResultDto result = service.check(input);

        assertThat(result.match()).isTrue();
        assertThat(result.inputName()).isEqualTo(input);
        assertThat(result.normalizedInput()).isEqualTo(normalized);
        assertThat(result.matchedName()).isEqualTo(name.getName());
        assertThat(result.similarityScore()).isEqualTo(0.91);
    }

    @Test
    void shouldReturnNoMatchWhenNoneFound() {
        String input = "Some Unknown Name";
        String normalized = TextNormalizer.normalize(input);

        when(repository.findAll()).thenReturn(List.of());
        when(matcher.match(eq(normalized), anyList())).thenReturn(Optional.empty());

        MatchResultDto result = service.check(input);

        assertThat(result.match()).isFalse();
        assertThat(result.inputName()).isEqualTo(input);
        assertThat(result.normalizedInput()).isEqualTo(normalized);
        assertThat(result.matchedName()).isNull();
        assertThat(result.similarityScore()).isEqualTo(0.0);
    }
}
