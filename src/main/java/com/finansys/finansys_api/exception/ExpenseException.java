package com.finansys.finansys_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExpenseException extends RuntimeException {

    public ExpenseException(String message) {
        super(message);
    }

    public ExpenseException(String message, Throwable cause) {
        super(message, cause);
    }
}
