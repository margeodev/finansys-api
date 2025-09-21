package com.finansys.finansys_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String field, String value) {
        super(String.format("Usuário com %s '%s' não encontrado.", field, value));
    }
}