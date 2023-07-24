package com.target.targetreadyresultsservice.repository;

import com.target.targetreadyresultsservice.model.ClassLevel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends MongoRepository<ClassLevel, String> {

    Optional<ClassLevel> findByName(String name);
}
