package com.swadlok.exception;

public class CategoryException extends ApplicationException {

    public CategoryException(String message) {
        super(message);
    }

    public CategoryException(String message, Throwable cause) {
        super(message, cause);
    }

}