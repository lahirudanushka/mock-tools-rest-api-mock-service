package com.open.mocktool.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MockCreateRequest {

    @NotBlank
    private String group;
    @NotBlank
    private String title;

    private String description;
    private List<Method> methodList = Collections.singletonList(Method.GET);
    private Map<String, String> responseHeaders;
    private Integer responseStatus = 200;
    private Long serverDelay = 0L;
    private Object responseBody;

}
