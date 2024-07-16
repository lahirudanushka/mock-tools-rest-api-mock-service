package com.open.mocktool.service;


import com.open.mocktool.repository.Mock;
import com.open.mocktool.repository.MockRepository;
import com.open.mocktool.util.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class BusinessService {

    @Autowired
    private MockRepository mockRepository;

    @Autowired
    private DelayService delayService;

    @Value("${daily-request-limit}")
    private Long limit;

    @Value("${daily-request-limit-flag}")
    private boolean limitFlag;

    public CompletableFuture<ResponseEntity<Object>> getResponse(String id, String method) {
        try {
            Optional<Mock> mock = mockRepository.findById(id);
            if (mock.isPresent()) {
                Mock m = mock.get();
                if (limitFlag && (m.getLastUsedDateTime() != null && m.getLastUsedDateTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) && (m.getUsage() == limit || m.getUsage() > limit))
                    throw new CommonException("509", "Request Limit Quota Exceeded. Please Try Again Next Day.", HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, null);
                if (!m.getMethodList().stream().filter(e -> e.name().equalsIgnoreCase(method)).findAny().isPresent())
                    throw new CommonException("400", "Request Method Not Allowed.", HttpStatus.METHOD_NOT_ALLOWED, null);
                HttpHeaders headers = new HttpHeaders();
                if (m.getResponseHeaders() != null)
                    m.getResponseHeaders().forEach((key, value) -> headers.add(key, value));

                if (m.getLastUsedDateTime() != null && m.getLastUsedDateTime().getDayOfMonth() != LocalDateTime.now().getDayOfMonth())
                    m.setUsage(1L);
                else
                    m.setUsage(m.getUsage() + 1);


                m.setLastUsedDateTime(LocalDateTime.now());
                mockRepository.save(m);

                return delayService.delay(m.getServerDelay()).thenApply(v -> ResponseEntity.status(m.getResponseStatus()).headers(headers).body(m.getResponseBody()));
            }
            throw new CommonException("404", "Mock is not found.", HttpStatus.NOT_FOUND, null);
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }

    }
}
