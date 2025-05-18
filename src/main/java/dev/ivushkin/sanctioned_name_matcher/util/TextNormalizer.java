package dev.ivushkin.sanctioned_name_matcher.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TextNormalizer {

    private static final Set<String> NOISE_WORDS = Set
            .of("mr", "mrs", "ms", "the", "and", "to", "an", "a", "of", "dr", "sir", "miss");

    public static String normalize(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        return Arrays.stream(
                        input.toLowerCase()
                                .replaceAll("[^a-z0-9]", " ") // удаление пунктуации
                                .split("\\s+")
                )
                .filter(token -> !NOISE_WORDS.contains(token))
                .sorted()
                .collect(Collectors.joining(" "));
    }

}
