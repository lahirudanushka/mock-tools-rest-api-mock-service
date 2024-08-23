package com.open.mocktool.controller;

import com.open.mocktool.service.BusinessService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@Hidden
@Tag(name = "Mock Operation Controller", description = "All Mocks Are Served With These APIs")
@CrossOrigin("*")
@RequestMapping(value = "/api")
public class BusinessController {

    @Autowired
    private BusinessService apiService;

    @RequestMapping(value = {"/{id}","/{id}/{path_1}","/{id}/{path_1}/{path_2}","/{id}/{path_1}/{path_2}/{path_3}", "/{id}/**"})
    CompletableFuture<ResponseEntity<Object>> getResponse(@PathVariable("id") String id,
                                                          @PathVariable Map<String, String> allPathVars,
                                                          @RequestParam Map<String, String> allParams,
                                                          @RequestHeader Map<String, String> allHeaders,
                                                          @RequestBody(required = false) String requestBody,
                                                          HttpServletRequest request) {
        return apiService.getResponse(id,allParams, allHeaders,requestBody,request.getMethod(),allPathVars);
    }
}
