package com.open.mocktool.repository;

import com.open.mocktool.dto.Method;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Mocks")
public class Mock {

    @Id
    private String id;
    private String group;
    private String title;
    private String description;
    private List<Method> methodList;
    private Map<String, String> responseHeaders;
    private Integer responseStatus;
    private Long serverDelay;
    private String owner;
    private Object responseBody;

    private Long usage;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;


}
