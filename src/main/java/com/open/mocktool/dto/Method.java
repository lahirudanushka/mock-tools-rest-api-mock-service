package com.open.mocktool.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Method {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE,
}
