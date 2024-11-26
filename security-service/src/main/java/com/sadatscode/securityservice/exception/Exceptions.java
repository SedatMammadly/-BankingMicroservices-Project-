package com.sadatscode.securityservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Exceptions{

   ExistingUserException(HttpStatus.INTERNAL_SERVER_ERROR,ExceptionMessages.USER_ALREADY_EXISTS),
   NotFoundException(HttpStatus.NOT_FOUND,ExceptionMessages.USER_NOT_FOUND);

    private final HttpStatus httpStatus;
    private final String message;
}
