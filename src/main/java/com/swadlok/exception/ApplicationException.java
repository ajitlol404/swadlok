package com.swadlok.exception;

public class ApplicationException extends RuntimeException {

    public ApplicationException() {
        super("Application error occurred");
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

}
