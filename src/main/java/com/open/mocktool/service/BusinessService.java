package com.open.mocktool.service;


import com.open.mocktool.repository.Mock;
import com.open.mocktool.repository.MockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessService {

    @Autowired
    private MockRepository mockRepository;

    public ResponseEntity<Object> getResponse(String id, String method) {
        Optional<Mock> mock = mockRepository.findById(id);
        if (mock.isPresent()) {
            Mock m = mock.get();
            if (!m.getMethodList().stream().filter(e -> e.name().equalsIgnoreCase(method)).findAny().isPresent())
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            HttpHeaders headers = new HttpHeaders();
            if (m.getResponseHeaders() != null)
                m.getResponseHeaders().forEach((key, value) -> headers.add(key, value));
            return ResponseEntity.status(m.getResponseStatus()).headers(headers).body(m.getResponseBody());
        }
        return ResponseEntity.notFound().build();
    }
}
