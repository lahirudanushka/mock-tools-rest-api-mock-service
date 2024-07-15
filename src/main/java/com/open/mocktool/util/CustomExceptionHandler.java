package com.open.mocktool.util;


import com.open.mocktool.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(CommonException.class)
    public <T> ResponseEntity<ExceptionResponse> customerCommonExceptionHandler(CommonException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(ex.getCode());
        exceptionResponse.setMessage(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(exceptionResponse);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public <T> ResponseEntity<ExceptionResponse> accessDeniedExceptionHandler(Exception ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode("401");
        exceptionResponse.setMessage("Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<ExceptionResponse> customExceptionHandler(Exception ex) throws Exception {

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode("500");
        exceptionResponse.setMessage("Internal Server Error | " + ex.getMessage());
        if (ex.getStackTrace() != null && ex.getStackTrace().length > 0)
            exceptionResponse.setTrace(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }
}
