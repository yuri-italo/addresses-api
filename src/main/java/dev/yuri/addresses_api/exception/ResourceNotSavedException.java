package dev.yuri.addresses_api.exception;

public class ResourceNotSavedException extends RuntimeException {
    public ResourceNotSavedException() {
        super();
    }

    public ResourceNotSavedException(String message) {
        super(message);
    }
}
