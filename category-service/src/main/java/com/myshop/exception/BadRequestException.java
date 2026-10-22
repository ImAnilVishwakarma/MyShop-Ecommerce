package com.myshop.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BadRequestException extends RuntimeException {
    private String message;
    private HttpStatus status;

    public BadRequestException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;

    }
}


