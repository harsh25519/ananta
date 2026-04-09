package dev.hkb.ananta.exceptionHandler;

public class ManufacturerNotFound extends RuntimeException{
    public ManufacturerNotFound(String message) {
        super(message);
    }

    public ManufacturerNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ManufacturerNotFound(Throwable cause) {
        super(cause);
    }
}
