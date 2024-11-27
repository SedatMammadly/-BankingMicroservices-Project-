package com.sadatscode.securityservice.auth;

import com.sadatscode.securityservice.model.Users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(registerRequest.getPassword()))")
//    @Mapping(target ="role",ignore = true )
//    @Mapping(target ="isEnabled",ignore = true )
//    @Mapping(target = "createdAt",ignore = true)
//    @Mapping(target = "id",ignore = true)
    Users dtoToEntity(RegisterRequest registerRequest,PasswordEncoder passwordEncoder);
}
