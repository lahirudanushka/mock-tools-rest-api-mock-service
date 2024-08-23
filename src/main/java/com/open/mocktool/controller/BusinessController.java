package com.open.mocktool.controller;

import com.open.mocktool.service.BusinessService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@Hidden
@Tag(name = "Mock Operation Controller", description = "All Mocks Are Served With These APIs")
@CrossOrigin("*")
@RequestMapping(value = "/api")
public class BusinessController {

    @Autowired
    private BusinessService apiService;

    @RequestMapping(value = {"/{id}", "/{id}/**"})
    CompletableFuture<ResponseEntity<Object>> getResponse(@PathVariable String id, HttpServletRequest request) {
        return apiService.getResponse(id, request.getMethod());
    }
}
