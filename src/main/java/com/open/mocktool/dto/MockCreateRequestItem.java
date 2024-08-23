package com.open.mocktool.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MockCreateRequestItem {
    @Schema(description = "Instance Id")
    private Integer id ;
    @Schema(description = "Http method", defaultValue = "[\"GET\", \"POST\", \"DELETE\", \"PUT\", \"OPTIONS\", \"HEAD\", \"PATCH\", \"TRACE\"]")
    private List<Method> methodList ;
    @Schema(description = "Response Headers", defaultValue = "{\"Content-Type\":\"application/json\"}")
    private Map<String, String> responseHeaders;
    @Schema(description = "Http status code", defaultValue = "200")
    private Integer responseStatus ;
    @Schema(description = "Http status code", defaultValue = "0")
    private Long serverDelay;
    private Object responseBody;
}
