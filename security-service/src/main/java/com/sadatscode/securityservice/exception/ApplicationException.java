package com.sadatscode.securityservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final Exceptions exceptions;
    private final String message;

    public ApplicationException(Exceptions exceptions, String customMessage) {
        super(customMessage);
        this.exceptions = exceptions;
        this.message = customMessage;
    }

    public ApplicationException(Exceptions exceptions) {
        super(exceptions.getMessage());
        this.exceptions = exceptions;
        this.message = exceptions.getMessage();
    }

    public HttpStatus status() {
        return exceptions.getHttpStatus();
    }


    @Override
    public String getMessage() {
        return message != null ? message : exceptions.getMessage();
    }
}
