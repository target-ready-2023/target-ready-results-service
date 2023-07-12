package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.controller.ResultsController;
import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResultsService {
    private final StudentRepository studentRepository;

    @Autowired
    public ResultsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(ResultsController.class);

    public Optional<Student> getStudentInfo(String rollNumber) {
        log.info("fetching roll number {} from db", rollNumber);
        return studentRepository.findById(rollNumber);
    }

    public void setStudentInfo(Student student) {
        try {
            log.info("storing student {} into db", student);
            studentRepository.save(student);
        } catch (Exception e) {
            log.info("error while storing student {} into db", e.toString());
        }
    }
}