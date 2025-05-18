package dev.ivushkin.sanctioned_name_matcher.exception;

public class DuplicateSanctionedNameException extends RuntimeException {
    public DuplicateSanctionedNameException(String originalName) {
        super("Sanctioned name already exists for input: \"" + originalName + "\"");
    }
}
