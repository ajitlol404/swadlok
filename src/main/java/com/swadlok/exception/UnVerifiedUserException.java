package com.swadlok.exception;

public class UnVerifiedUserException extends ApplicationException {

    public UnVerifiedUserException(String message) {
        super(message);
    }

    public UnVerifiedUserException(String message, Throwable cause) {
        super(message, cause);
    }

}