package dev.ivushkin.sanctioned_name_matcher.exception;

public class EmptyNormalizedNameException extends RuntimeException{

    public EmptyNormalizedNameException(String originalName) {
        super("Normalized name is empty for input: '" + originalName + "'");
    }
}
