package com.target.targetreadyresultsservice.repository;

import com.target.targetreadyresultsservice.model.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
List<Subject>findBySubjectName(String subjectName);
}
