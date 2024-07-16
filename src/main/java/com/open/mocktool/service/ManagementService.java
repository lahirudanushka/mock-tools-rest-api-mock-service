package com.open.mocktool.service;

import com.open.mocktool.dto.MockCreateRequest;
import com.open.mocktool.repository.Mock;
import com.open.mocktool.repository.MockRepository;
import com.open.mocktool.util.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ManagementService {

    @Autowired
    private MockRepository mockRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Value("${base-url}")
    private String baseUrl;

    public ResponseEntity<Object> createMock(MockCreateRequest body, HttpServletRequest httpServletRequest) {
        try {
            Mock mock = new Mock();
            mock.setId(UUID.randomUUID().toString());
            mock.setOwner(tokenProvider.extractUsername(tokenProvider.tokenExtractor(httpServletRequest)));
            mock.setCreatedDateTime(LocalDateTime.now());
            mock.setUsage(0L);
            mapMock(body, mock);
            mock = mockRepository.save(mock);
            mock.setUrl(baseUrl + mock.getId());
            return ResponseEntity.ok(mock);
        } catch (CommonException e) {
            throw e;
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
        mock.setResponseBody(body.getResponseBody());
        mock.setUpdatedDateTime(LocalDateTime.now());
        mock.setIsShared(body.getIsShared());
    }

    public ResponseEntity<Object> getAllMocks(Integer pageNumber, Integer pageSize, HttpServletRequest httpServletRequest) {
        try {
            String owner = tokenProvider.extractUsername(tokenProvider.tokenExtractor(httpServletRequest));
            Page<Mock> allMocks = mockRepository.findAllByOwner(owner, PageRequest.of(pageNumber, pageSize == 0 ? 0 : pageSize + 1));
            return ResponseEntity.ok(allMocks.get().map(m -> {
                m.setUrl(baseUrl + m.getId());
                return m;
            }));
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }

    public ResponseEntity<Object> getMockById(String id, HttpServletRequest httpServletRequest) {
        try {
            String owner = tokenProvider.extractUsername(tokenProvider.tokenExtractor(httpServletRequest));
            Optional<Mock> mock = mockRepository.findById(id);
            if (mock.isPresent()) {
                if ((mock.get().getIsShared() != null && mock.get().getIsShared().booleanValue()) || (mock.get().getOwner().equalsIgnoreCase(owner))) {
                    Mock m = mock.get();
                    m.setUrl(baseUrl + m.getId());
                    return ResponseEntity.ok(mock.get());
                } else
                    throw new CommonException("302", "You dont have access to this mock resource", HttpStatus.FORBIDDEN, null);
            } else
                throw new CommonException("404", "Mock Not Found", HttpStatus.NOT_FOUND, null);

        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }

    public ResponseEntity<Object> updateMock(MockCreateRequest body, String id, HttpServletRequest httpServletRequest) {
        try {
            String owner = tokenProvider.extractUsername(tokenProvider.tokenExtractor(httpServletRequest));
            Optional<Mock> mock = mockRepository.findById(id);
            if (mock.isPresent()) {
                if ((mock.get().getIsShared() != null && mock.get().getIsShared().booleanValue()) || (mock.get().getOwner().equalsIgnoreCase(owner))) {
                    Mock updated = mock.get();
                    updated.setLastModifiedBy(tokenProvider.extractUsername(tokenProvider.tokenExtractor(httpServletRequest)));
                    mapMock(body, updated);
                    updated = mockRepository.save(updated);
                    updated.setUrl(baseUrl + updated.getId());
                    return ResponseEntity.accepted().body(updated);
                } else
                    throw new CommonException("302", "You dont have access to this mock resource", HttpStatus.FORBIDDEN, null);
            } else
                throw new CommonException("404", "Mock Not Found", HttpStatus.NOT_FOUND, null);
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }

    public ResponseEntity<Object> deleteMockById(String id, HttpServletRequest httpServletRequest) {
        try {
            String owner = tokenProvider.extractUsername(tokenProvider.tokenExtractor(httpServletRequest));
            Optional<Mock> mock = mockRepository.findById(id);
            if (mock.isPresent()) {
                if ((mock.get().getIsShared() != null && mock.get().getIsShared().booleanValue()) || (mock.get().getOwner().equalsIgnoreCase(owner))) {
                    mockRepository.delete(mock.get());
                    return ResponseEntity.noContent().build();
                } else
                    throw new CommonException("302", "You dont have access to this mock resource", HttpStatus.FORBIDDEN, null);
            } else
                throw new CommonException("404", "Mock Not Found", HttpStatus.NOT_FOUND, null);
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }
}
