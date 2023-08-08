package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
    public ResponseEntity<Student> getStudentDetails(
            @RequestParam("studentId") String studentId
    ) {
        try {
            log.info("get student info with Student Id {}", studentId);
            Optional<Student> studentInfo = studentService.getStudentInfo(studentId);
            return studentInfo.map(
                    student -> new ResponseEntity<>(student, HttpStatus.OK)
            ).orElseGet(
                    () -> new ResponseEntity("Not Found : ", HttpStatus.NOT_FOUND)
            );
        } catch (Exception e) {
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Error", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/student")
    public ResponseEntity<Student> setStudentDetails(@RequestBody Student studentInfo) {
        try {
            studentService.setStudentInfo(studentInfo);
            return new ResponseEntity<>(studentInfo, HttpStatus.OK);
        } catch (Exception e) {
            log.info("exception occurred");
            return new ResponseEntity("Error", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/student/{classCode}")
    public ResponseEntity<List<Student>> getStudentsByClassCode(@PathVariable String classCode){
        try{
            List<Student> students=studentService.getStudentDetailsByClassCode(classCode);
            if ((students != null) && !(students.toString().isEmpty())) {
                log.info("students found successfully as - {}",students);
                return new ResponseEntity<>(students, HttpStatus.OK);
            }
            else{
                log.info("No classes found");
                return new ResponseEntity(students, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
        }
    }
    @GetMapping("/student/search")
    public ResponseEntity<List<ClassDto>> SearchStudentBYName(@RequestParam(required = false) String studentName){
        try {
            List<Student> students = new ArrayList<>();
            if(studentName.isBlank()){
                students=studentService.getAllStudents();
            }
            else{
                students= studentService.getStudentByName(studentName);
            }
            if (students==null || students.isEmpty()) {
                return new ResponseEntity("No data found", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity(students, HttpStatus.OK);
            }
        } catch(Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

    }
    @GetMapping("/{roll}/{className}")
    public ResponseEntity<Student> getStudentFromRollClass(@PathVariable String roll, @PathVariable String className){
        try {
            Student s = studentService.getStudentFromClassRollNo(className,roll);
            return new ResponseEntity<>(s,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }


}
