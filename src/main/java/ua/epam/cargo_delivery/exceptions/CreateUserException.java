package ua.epam.cargo_delivery.exceptions;

public class CreateUserException extends RuntimeException {

    public CreateUserException(String message) {
        super(message);
    }

    public CreateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
