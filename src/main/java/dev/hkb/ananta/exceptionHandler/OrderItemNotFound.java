package dev.hkb.ananta.exceptionHandler;

public class OrderItemNotFound extends RuntimeException{
    public OrderItemNotFound(String message) {
        super(message);
    }

    public OrderItemNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderItemNotFound(Throwable cause) {
        super(cause);
    }
}
