package dev.hkb.ananta.exceptionHandler;

public class CartNotFound extends RuntimeException{
    public CartNotFound(String message) {
        super(message);
    }

    public CartNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public CartNotFound(Throwable cause) {
        super(cause);
    }
}
