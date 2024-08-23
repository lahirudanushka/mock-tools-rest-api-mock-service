package com.open.mocktool.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

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

    private List<MockCreateRuleMap> responseRules;

    @Schema(description = "Mock Instances")
    List<MockCreateRequestItem> mockInstances;

    @Schema(description = "Is this a shared manageable mock?", defaultValue = "false")
    @NotNull
    private Boolean isShared;
}
