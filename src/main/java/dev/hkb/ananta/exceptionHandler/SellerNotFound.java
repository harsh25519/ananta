package dev.hkb.ananta.exceptionHandler;

public class SellerNotFound extends RuntimeException{
    public SellerNotFound(String message) {
        super(message);
    }

    public SellerNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public SellerNotFound(Throwable cause) {
        super(cause);
    }
}
