package dev.hkb.ananta.exceptionHandler;

public class ReviewNotFound extends RuntimeException{
    public ReviewNotFound(String message) {
        super(message);
    }

    public ReviewNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewNotFound(Throwable cause) {
        super(cause);
    }
}
