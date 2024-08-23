package com.open.mocktool.controller;

import com.open.mocktool.dto.MockCreateRequest;
import com.open.mocktool.service.ManagementService;
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

@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Mock Management Controller", description = "APIs for managing mocks")
@CrossOrigin("*")
@RequestMapping(value = "/admin")
public class ManagementController {

    @Autowired
    private ManagementService service;


    @Operation(summary = "Create Mock", description = "Create Mock API")
    @PostMapping(value = "/mock", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> createMock(@Valid @RequestBody MockCreateRequest body, HttpServletRequest httpServletRequest) {
        return service.createMock(body, httpServletRequest);
    }

    @Operation(summary = "Update Mock", description = "Update Mock By Id")
    @PatchMapping(value = "/mock/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> updateMock(@Valid @RequestBody MockCreateRequest body, @PathVariable String id, HttpServletRequest httpServletRequest) {
        return service.updateMock(body, id, httpServletRequest);
    }


    @Operation(summary = "Get Mocks", description = "Get Mocks List With Pagination")
    @GetMapping(value = "/mock", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getAllMocks(@RequestParam(defaultValue = "0",required = false) Integer pageNumber, @RequestParam(defaultValue = "10",required = false) Integer pageSize, HttpServletRequest request) {
        return service.getAllMocks(pageNumber, pageSize, request);
    }

    @Operation(summary = "Get Mock", description = "Get Mock By ID")
    @GetMapping(value = "/mock/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getMockById(@PathVariable String id, HttpServletRequest request) {
        return service.getMockById(id, request);
    }


    @Operation(summary = "Delete Mock", description = "Delete Mock By ID")
    @DeleteMapping(value = "/mock/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> deleteMockById(@PathVariable String id, HttpServletRequest request) {
        return service.deleteMockById(id, request);
    }
}
