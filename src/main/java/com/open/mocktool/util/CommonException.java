package com.open.mocktool.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CommonException extends RuntimeException{

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
    private final StackTraceElement[] stackTraceElements;

    public CommonException(String code, String message, HttpStatus httpStatus, StackTraceElement[] stackTraceElements){
        super(message);
        this.stackTraceElements = stackTraceElements;
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
