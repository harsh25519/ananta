package dev.hkb.ananta.exceptionHandler;

public class UserNotAuthenticated extends RuntimeException{
    public UserNotAuthenticated(String message) {
        super(message);
    }

    public UserNotAuthenticated(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotAuthenticated(Throwable cause) {
        super(cause);
    }
}
