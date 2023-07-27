package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/student/v1")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService resultsService) {
        this.studentService = resultsService;
    }

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @GetMapping("/student")
    public ResponseEntity<String> getStudentDetails(
            @RequestParam("studentId") String studentId
    ) {
        try {
            log.info("get student info with Student Id {}", studentId);
            Optional<Student> studentInfo = studentService.getStudentInfo(studentId);
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
            @RequestParam("studentId") String studentId,
            @RequestParam("name") String name,
            @RequestParam("classCode") String classCode,
             @RequestParam("rollNumber") String rollNumber

    ) {
        try {
            Student student = new Student(studentId, name, classCode,rollNumber );

            log.info("set student info with studentId {}", studentId);
            studentService.setStudentInfo(student);

            return new ResponseEntity<>("Successful", HttpStatus.OK);
        } catch (Exception e) {
            log.info("exception occurred");
            return new ResponseEntity<>("Error", HttpStatus.EXPECTATION_FAILED);
        }
    }

}
