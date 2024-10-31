package dev.yuri.addresses_api.exception;

public class InvalidFilterException extends RuntimeException {
    public InvalidFilterException() {
        super();
    }

    public InvalidFilterException(String message) {
        super(message);
    }
}

