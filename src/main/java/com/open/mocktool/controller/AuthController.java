package com.open.mocktool.controller;

import com.open.mocktool.dto.LoginRequest;
import com.open.mocktool.service.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@Tag(name = "Authentication Controller", description = "Authentication Related APIs")
@CrossOrigin("*")
@RequestMapping(value = "/auth")
public class AuthController {


    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "Login", description = "Login With User Name AND Password")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest body) {
        String token = jwtTokenProvider.generateToken(body);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }


    @Operation(summary = "Refresh Token", description = "Refresh Access JWT Token")
    @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<String, String>> refresh(@RequestHeader(name = "Authorization") String auth, HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.refresh(httpServletRequest);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}
