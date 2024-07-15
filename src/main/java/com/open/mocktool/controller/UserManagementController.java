package com.open.mocktool.controller;

import com.open.mocktool.dto.UserCreateRequest;
import com.open.mocktool.dto.UserUpdateRequest;
import com.open.mocktool.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasAnyRole('ADMIN')")
@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Management Controller", description = "APIs for managing users")
@RequestMapping(value = "/admin")
public class UserManagementController {

    @Autowired
    private UserManagementService service;

    @Operation(summary = "Create User", description = "Create User API")
    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> createUser(@Valid @RequestBody UserCreateRequest body) {
        return service.createUser(body);
    }

    @Operation(summary = "Update User", description = "Update User By Id")
    @PatchMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> updateUser(@Valid @RequestBody UserUpdateRequest body, @PathVariable String id) {
        return service.updateUser(body, id);
    }

    @Operation(summary = "Get User", description = "Get User List With Pagination")
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return service.getAllUsers(pageNumber, pageSize);
    }

    @Operation(summary = "Get User", description = "Get User By ID")
    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getUserById(@PathVariable String id, HttpServletRequest request) {
        return service.getUserById(id);
    }

    @Operation(summary = "Delete User", description = "Delete User By ID")
    @DeleteMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> deleteUserById(@PathVariable String id, HttpServletRequest request) {
        return service.deleteUserById(id);
    }
}
