package com.open.mocktool.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Group Name", defaultValue = "Test Group")
    private String group;
    @NotBlank
    @Schema(description = "Mock Name", defaultValue = "Test Mock API")
    private String title;


    @Schema(description = "Mock Description", defaultValue = "Test Mock API")
    private String description;

    @NotBlank
    @Schema(description = "Http method", defaultValue = "[\"GET\"]")
    private List<Method> methodList ;


    private Map<String, String> responseHeaders;

    @Schema(description = "Response status code", defaultValue = "200")
    private Integer responseStatus = 200;

    @Schema(description = "Server Delay in Seconds- 5s Max")
    private Long serverDelay = 0L;
    private Object responseBody;

}
