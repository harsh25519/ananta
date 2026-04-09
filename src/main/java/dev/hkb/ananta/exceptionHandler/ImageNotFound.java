package dev.hkb.ananta.exceptionHandler;

public class ImageNotFound extends RuntimeException{
    public ImageNotFound(String message) {
        super(message);
    }

    public ImageNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageNotFound(Throwable cause) {
        super(cause);
    }
}
