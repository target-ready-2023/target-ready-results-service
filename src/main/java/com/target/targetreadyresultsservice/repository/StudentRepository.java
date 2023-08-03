package com.target.targetreadyresultsservice.repository;

import com.target.targetreadyresultsservice.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    List<Student> findByClassCode(String classCode);
    List<Student> findByName(String studentName);
}
