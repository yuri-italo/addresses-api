package dev.yuri.addresses_api.exception;

public class AddressDoesNotBelongToPersonException extends RuntimeException {
    public AddressDoesNotBelongToPersonException() {
    }

    public AddressDoesNotBelongToPersonException(String message) {
        super(message);
    }
}
