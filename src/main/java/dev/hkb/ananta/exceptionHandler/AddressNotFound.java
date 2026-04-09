package dev.hkb.ananta.exceptionHandler;

public class AddressNotFound extends RuntimeException{
    public AddressNotFound(String message) {
        super(message);
    }

    public AddressNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressNotFound(Throwable cause) {
        super(cause);
    }
}
