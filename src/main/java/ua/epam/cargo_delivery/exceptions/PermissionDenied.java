package ua.epam.cargo_delivery.exceptions;

public class PermissionDenied extends RuntimeException {

    public PermissionDenied(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionDenied(String message) {
        super(message);
    }
}
