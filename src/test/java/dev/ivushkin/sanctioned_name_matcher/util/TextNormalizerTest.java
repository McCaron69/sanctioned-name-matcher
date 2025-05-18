package dev.ivushkin.sanctioned_name_matcher.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextNormalizerTest {

    @Test
    void shouldNormalizeSimpleName() {
        String result = TextNormalizer.normalize("Osama Bin Laden");

        assertThat(result).isEqualTo("bin laden osama");
    }

    @Test
    void shouldRemovePunctuationAndNoiseWords() {
        String result = TextNormalizer.normalize("Dr. Osama, the Bin-Laden!");

        assertThat(result).isEqualTo("bin laden osama");
    }

    @Test
    void shouldSortWordsAlphabetically() {
        String result = TextNormalizer.normalize("Laden Bin Osama");

        assertThat(result).isEqualTo("bin laden osama");
    }

    @Test
    void shouldReturnEmptyStringForOnlyNoiseWords() {
        String result = TextNormalizer.normalize("Mr. the and a");

        assertThat(result).isEqualTo("");
    }

    @Test
    void shouldHandleNullAndBlank() {
        assertThat(TextNormalizer.normalize(null)).isEqualTo("");
        assertThat(TextNormalizer.normalize("   ")).isEqualTo("");
    }
}
