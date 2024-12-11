package com.sadatscode.securityservice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyRequest {
    String email;
    String verifyCode;
}
