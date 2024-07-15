package com.open.mocktool.dto;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String code;
    private String message;
    private String trace;

}
