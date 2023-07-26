package com.target.targetreadyresultsservice.repository;

import com.target.targetreadyresultsservice.model.Results;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultsRepository extends MongoRepository<Results, String> {
}
