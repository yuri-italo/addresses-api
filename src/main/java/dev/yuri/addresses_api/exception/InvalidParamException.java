package dev.yuri.addresses_api.exception;

public class InvalidParamException extends RuntimeException {
    public InvalidParamException() {
        super();
    }

    public InvalidParamException(String message) {
        super(message);
    }
}

