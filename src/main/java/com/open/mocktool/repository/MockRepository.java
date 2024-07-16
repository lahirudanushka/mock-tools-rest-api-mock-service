package com.open.mocktool.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.List;

@Repository
public interface MockRepository extends MongoRepository<Mock,String> {
    List<Mock> findAllByOwner(String owner);

    @Query("{ $or: [ {'owner': ?0}, {'isShared':  true} ]}")
    Page<Mock> findAllByOwner(String owner, Pageable pageable);

}
