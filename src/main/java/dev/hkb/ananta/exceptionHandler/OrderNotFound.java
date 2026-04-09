package dev.hkb.ananta.exceptionHandler;

public class OrderNotFound extends RuntimeException{
    public OrderNotFound(String message) {
        super(message);
    }

    public OrderNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFound(Throwable cause) {
        super(cause);
    }
}
