package dev.hkb.ananta.exceptionHandler;

public class CartItemNotFound extends RuntimeException{
    public CartItemNotFound(String message) {
        super(message);
    }

    public CartItemNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public CartItemNotFound(Throwable cause) {
        super(cause);
    }
}
