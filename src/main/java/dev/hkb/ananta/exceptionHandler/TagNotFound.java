package dev.hkb.ananta.exceptionHandler;

public class TagNotFound extends RuntimeException{
    public TagNotFound(String message) {
        super(message);
    }

    public TagNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNotFound(Throwable cause) {
        super(cause);
    }
}
