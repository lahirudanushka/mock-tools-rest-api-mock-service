package com.open.mocktool.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MockCreateRuleMapItem {
    private String path;
    private String value;
    @Schema(description = "Rule Type", defaultValue = "PARAM")
    private Rule ruleType;
}
