package com.open.mocktool.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String email;

//    @NotBlank
    private String team;

    @NotBlank
    private RoleEnum role;


}


