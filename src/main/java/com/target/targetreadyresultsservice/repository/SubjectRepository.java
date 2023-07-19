package com.target.targetreadyresultsservice.repository;

import com.target.targetreadyresultsservice.model.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {

}
