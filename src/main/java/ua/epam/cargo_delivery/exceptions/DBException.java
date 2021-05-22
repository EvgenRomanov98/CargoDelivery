package ua.epam.cargo_delivery.exceptions;

public class DBException extends RuntimeException {
    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}
