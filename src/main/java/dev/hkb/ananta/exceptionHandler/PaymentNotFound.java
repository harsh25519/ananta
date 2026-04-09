package dev.hkb.ananta.exceptionHandler;

public class PaymentNotFound extends RuntimeException{
    public PaymentNotFound(String message) {
        super(message);
    }

    public PaymentNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentNotFound(Throwable cause) {
        super(cause);
    }
}
