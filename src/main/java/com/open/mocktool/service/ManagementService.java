package com.open.mocktool.service;

import com.open.mocktool.dto.MockCreateRequest;
import com.open.mocktool.repository.Mock;
import com.open.mocktool.repository.MockRepository;
import com.open.mocktool.util.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ManagementService {

    @Autowired
    private MockRepository mockRepository;

    public ResponseEntity<Object> createMock(MockCreateRequest body) {
        try {
            Mock mock = new Mock();
            mock.setId(UUID.randomUUID().toString());
            mapMock(body, mock);
            mock = mockRepository.save(mock);
            return ResponseEntity.ok(mock);
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }

    private static void mapMock(MockCreateRequest body, Mock mock) {

        mock.setGroup(body.getGroup());
        mock.setTitle(body.getTitle());
        mock.setDescription(body.getDescription());
        mock.setMethodList(body.getMethodList());
        mock.setResponseHeaders(body.getResponseHeaders());
        mock.setResponseStatus(body.getResponseStatus());
        mock.setServerDelay(body.getServerDelay());
        mock.setOwner("GlobalAdmin");
        mock.setResponseBody(body.getResponseBody());
        mock.setUsage(0L);
        mock.setCreatedDateTime(LocalDateTime.now());
        mock.setUpdatedDateTime(LocalDateTime.now());
    }

    public ResponseEntity<Object> getAllMocks(Integer pageNumber, Integer pageSize) {
        try {
            Page<Mock> allMocks = mockRepository.findAll(PageRequest.of(pageNumber, pageSize == 0 ? 0 : pageSize + 1));
            return ResponseEntity.ok(allMocks.get().collect(Collectors.toList()));
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }

    public ResponseEntity<Object> getMockById(String id) {
        try {
            return ResponseEntity.ok(mockRepository.findById(id));
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }

    public ResponseEntity<Object> updateMock(MockCreateRequest body, String id) {
        try {
            Optional<Mock> mock = mockRepository.findById(id);
            if (mock.isPresent()) {
                Mock updated = mock.get();
                mapMock(body, updated);
                updated = mockRepository.save(updated);
                return ResponseEntity.accepted().body(updated);
            } else
                throw new CommonException("404", "Mock Not Found", HttpStatus.NOT_FOUND, null);
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }

    public ResponseEntity<Object> deleteMockById(String id) {
        try {
            Optional<Mock> mock = mockRepository.findById(id);
            if (mock.isPresent()) {
                mockRepository.delete(mock.get());
                return ResponseEntity.noContent().build();
            } else
                throw new CommonException("404", "Mock Not Found", HttpStatus.NOT_FOUND, null);
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }
}
