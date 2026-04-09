package dev.hkb.ananta.exceptionHandler;

public class UserDoesNotExist extends RuntimeException{
    public UserDoesNotExist(String message) {
        super(message);
    }

    public UserDoesNotExist(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesNotExist(Throwable cause) {
        super(cause);
    }
}
