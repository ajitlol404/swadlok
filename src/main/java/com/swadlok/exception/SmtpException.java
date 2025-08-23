package com.swadlok.exception;

public class SmtpException extends ApplicationException {

    public SmtpException(String message) {
        super(message);
    }

    public SmtpException(String message, Throwable cause) {
        super(message, cause);
    }

}
