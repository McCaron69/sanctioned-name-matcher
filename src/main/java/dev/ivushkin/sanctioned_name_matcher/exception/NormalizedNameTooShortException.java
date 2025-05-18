package dev.ivushkin.sanctioned_name_matcher.exception;

public class NormalizedNameTooShortException extends RuntimeException {

    public NormalizedNameTooShortException(String normalizedName, int minLength) {
        super("Normalized name is too short: \"" + normalizedName + "\" (min length is " + minLength + ")");
    }
}
