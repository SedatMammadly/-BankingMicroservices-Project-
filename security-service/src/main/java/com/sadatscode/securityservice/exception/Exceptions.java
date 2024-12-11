package com.sadatscode.securityservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Exceptions{

   ExistingUserException(HttpStatus.INTERNAL_SERVER_ERROR,ExceptionMessages.USER_ALREADY_EXISTS),
   NotFoundException(HttpStatus.NOT_FOUND,ExceptionMessages.USER_NOT_FOUND),
   InvalidTokenException(HttpStatus.UNAUTHORIZED,ExceptionMessages.INVALID_TOKEN),
   OldPassDontMatchException(HttpStatus.BAD_REQUEST,ExceptionMessages.OLD_PASSWORD_DONT_MATCH),
   ConfirmPasswordDontMatchException(HttpStatus.BAD_REQUEST,ExceptionMessages.CONFIRM_PASSWORD_DONT_MATCH),
   NewPasswordSameException(HttpStatus.BAD_REQUEST,ExceptionMessages.NEW_PASSWORD_SAME);

    private final HttpStatus httpStatus;
    private final String message;
}
