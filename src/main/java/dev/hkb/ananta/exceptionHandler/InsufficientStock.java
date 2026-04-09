package dev.hkb.ananta.exceptionHandler;

public class InsufficientStock extends RuntimeException{
    public InsufficientStock(String message) {
        super(message);
    }

    public InsufficientStock(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientStock(Throwable cause) {
        super(cause);
    }
}
