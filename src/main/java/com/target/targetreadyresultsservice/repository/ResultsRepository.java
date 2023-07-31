package com.target.targetreadyresultsservice.repository;

import com.target.targetreadyresultsservice.model.Results;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ResultsRepository extends MongoRepository<Results, String> {
    List<Results> findAllByscheduleCode(String scheduleCode);
    List<Results> findAllBystudentId(String studentId);


}
