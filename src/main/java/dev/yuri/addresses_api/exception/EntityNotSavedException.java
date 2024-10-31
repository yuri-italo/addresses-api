package dev.yuri.addresses_api.exception;

public class EntityNotSavedException extends RuntimeException {
    public EntityNotSavedException() {
        super();
    }

    public EntityNotSavedException(String message) {
        super(message);
    }
}
