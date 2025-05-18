package dev.ivushkin.sanctioned_name_matcher.exception;

public class SanctionedNameNotFoundException extends RuntimeException {
    public SanctionedNameNotFoundException(long id) {
        super("Sanctioned name with id " + id + " not found");
    }
}
