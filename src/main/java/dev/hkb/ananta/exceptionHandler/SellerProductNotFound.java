package dev.hkb.ananta.exceptionHandler;

public class SellerProductNotFound extends RuntimeException{
    public SellerProductNotFound(String message) {
        super(message);
    }

    public SellerProductNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public SellerProductNotFound(Throwable cause) {
        super(cause);
    }
}
