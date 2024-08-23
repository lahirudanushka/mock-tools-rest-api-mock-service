package com.open.mocktool.service;


import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.open.mocktool.dto.MockCreateRequestItem;
import com.open.mocktool.dto.MockCreateRuleMap;
import com.open.mocktool.dto.MockCreateRuleMapItem;
import com.open.mocktool.repository.Mock;
import com.open.mocktool.repository.MockRepository;
import com.open.mocktool.util.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class BusinessService {

    @Autowired
    private MockRepository mockRepository;

    @Autowired
    private DelayService delayService;

    @Value("${daily-request-limit}")
    private Long limit;

    @Value("${daily-request-limit-flag}")
    private boolean limitFlag;

    public CompletableFuture<ResponseEntity<Object>> getResponse(String id, Map<String, String> allParams, Map<String, String> allHeaders, String requestBody, String method, Map<String, String> allPathVars) {
        try {
            Optional<Mock> mock = mockRepository.findById(id);
            if (mock.isPresent()) {
                Mock m = mock.get();

                MockCreateRequestItem qualifiedInstance = qualifyResponse(m, allParams, allHeaders, requestBody, method, allPathVars);

                if (limitFlag && (m.getLastUsedDateTime() != null && m.getLastUsedDateTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) && (m.getUsage() == limit || m.getUsage() > limit))
                    throw new CommonException("509", "Request Limit Quota Exceeded. Please Try Again Next Day.", HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, null);
                if (!qualifiedInstance.getMethodList().stream().filter(e -> e.name().equalsIgnoreCase(method)).findAny().isPresent())
                    throw new CommonException("400", "Request Method Not Allowed.", HttpStatus.METHOD_NOT_ALLOWED, null);
                HttpHeaders headers = new HttpHeaders();
                if (qualifiedInstance.getResponseHeaders() != null)
                    qualifiedInstance.getResponseHeaders().forEach((key, value) -> headers.add(key, value));

                if (m.getLastUsedDateTime() != null && m.getLastUsedDateTime().getDayOfMonth() != LocalDateTime.now().getDayOfMonth())
                    m.setUsage(1L);
                else
                    m.setUsage(m.getUsage() + 1);


                m.setLastUsedDateTime(LocalDateTime.now());
                mockRepository.save(m);

                return delayService.delay(qualifiedInstance.getServerDelay()).thenApply(v -> ResponseEntity.status(qualifiedInstance.getResponseStatus()).headers(headers).body(qualifiedInstance.getResponseBody()));
            }
            throw new CommonException("404", "Mock is not found.", HttpStatus.NOT_FOUND, null);
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }

    }

    private MockCreateRequestItem qualifyResponse(Mock m, Map<String, String> allParams, Map<String, String> allHeaders, String requestBody, String method, Map<String, String> allPathVars) {

        if (m.getResponseRules() == null || m.getResponseRules().isEmpty())
            return m.getMockInstances().get(0);


        List<MockCreateRuleMap> validRuleMap = new ArrayList<>();

        for (MockCreateRuleMap r : m.getResponseRules()) {
            if (isValidRule(r, allParams, allHeaders, requestBody, method, allPathVars))
                validRuleMap.add(r);
        }

        if (validRuleMap.isEmpty()) {
            log.warn("No rule maps qualified with the request data. Please check the rule configuration. Responded with default response.");
            return m.getMockInstances().get(0);
        }

        Integer instance = validRuleMap.get(0).getInstanceId();


        if (validRuleMap.size() > 1)
            log.warn("Multiple rule maps qualified with the request data. Please check the rule configuration.");


        Optional<MockCreateRequestItem> result = m.getMockInstances()
                .stream()
                .filter(item -> instance.equals(item.getId()))
                .findFirst();

        if (result.isPresent())
            return result.get();
        else {
            log.warn("Qualification Failed. Check the rule configuration. Responded with default response.");
            return m.getMockInstances().get(0);
        }


    }

    private boolean isValidRule(MockCreateRuleMap r, Map<String, String> allParams, Map<String, String> allHeaders, String requestBody, String method, Map<String, String> allPathVars) {
        boolean flag = true;
        for (MockCreateRuleMapItem rule : r.getRules()) {
            switch (rule.getRuleType()) {
                case PARAM:
                    flag = flag && allParams.getOrDefault(rule.getPath(), "").equals(rule.getValue());
                    break;
                case PATH:
                    flag = flag && pathQualify(rule, allPathVars);
                    break;
                case BODY:
                    flag = flag && bodyQualify(rule, requestBody);
                    break;
                case METHOD:
                    flag = flag && (rule.getValue() != null && method.equalsIgnoreCase(rule.getValue()) || rule.getPath() != null && method.equalsIgnoreCase(rule.getPath()));
                    break;
                case HEADER:
                    flag = flag && allHeaders.getOrDefault(rule.getPath(), "").equals(rule.getValue());
                    break;
            }
        }
        return flag;
    }

    private boolean bodyQualify(MockCreateRuleMapItem rule, String json) {
        boolean flag = true;

        try {
            ReadContext ctx = JsonPath.parse(json);
            Object result = ctx.read(rule.getPath());
            String bodyVal = result != null ? result.toString() : null;

            flag = flag && bodyVal != null && bodyVal.equals(rule.getValue());

        } catch (Exception e) {
            log.warn("Qualification Failed. Check the rule configuration. Exception in body value extraction.");
            flag = false;
        }


        return flag;
    }

    private boolean pathQualify(MockCreateRuleMapItem rule, Map<String, String> allPathVars) {

        boolean flag = true;

        List<String> pathList = Arrays.stream(rule.getPath().split("/")).toList();
        List<String> valueList = Arrays.stream(rule.getValue().split("/")).toList();

        for (int i = 0; i < pathList.size(); i++) {
            flag = flag && allPathVars.getOrDefault(pathList.get(i), "").equals(valueList.get(i));
        }

        return flag;
    }
}
