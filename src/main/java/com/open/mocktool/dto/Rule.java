package com.open.mocktool.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Rule {
    PARAM,
    PATH,
    BODY,
    HEADER,
    METHOD
}
