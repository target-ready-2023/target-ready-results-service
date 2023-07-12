package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.service.ResultsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/results/v1")
public class ResultsController {
    private final ResultsService resultsService;

    public ResultsController(ResultsService resultsService) {
        this.resultsService = resultsService;
    }

    private static final Logger log = LoggerFactory.getLogger(ResultsController.class);

    @GetMapping("/student")
    public ResponseEntity<String> getStudentDetails(
            @RequestParam("roll_no") String rollNumber
    ) {
        try {
            log.info("get student info with roll number {}", rollNumber);
            Optional<Student> studentInfo = resultsService.getStudentInfo(rollNumber);
            return studentInfo.map(
                    student -> new ResponseEntity<>("Found : " + student.toString(), HttpStatus.OK)
            ).orElseGet(
                    () -> new ResponseEntity<>("Not Found : ", HttpStatus.NOT_FOUND)
            );
        } catch (Exception e) {
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>("Error", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/student")
    public ResponseEntity<String> setStudentDetails(
            @RequestParam("roll_no") String rollNumber,
            @RequestParam("name") String name,
            @RequestParam("gender") String gender
    ) {
        try {
            Student student = new Student(rollNumber, name, gender);

            log.info("set student info with roll number {}", rollNumber);
            resultsService.setStudentInfo(student);

            return new ResponseEntity<>("Successful", HttpStatus.OK);
        } catch (Exception e) {
            log.info("exception occurred");
            return new ResponseEntity<>("Error", HttpStatus.EXPECTATION_FAILED);
        }
    }
}