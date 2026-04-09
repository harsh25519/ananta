package dev.hkb.ananta.exceptionHandler;

public class UserNotAuthorized extends RuntimeException{
    public UserNotAuthorized(String message) {
        super(message);
    }

    public UserNotAuthorized(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotAuthorized(Throwable cause) {
        super(cause);
    }
}
