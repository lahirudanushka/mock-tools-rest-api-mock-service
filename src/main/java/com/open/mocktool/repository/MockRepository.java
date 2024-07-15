package com.open.mocktool.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockRepository extends MongoRepository<Mock,String> {
    List<Mock> findAllByOwner(String owner);
}
