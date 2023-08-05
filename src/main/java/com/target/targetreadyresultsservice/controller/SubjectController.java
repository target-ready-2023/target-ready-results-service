package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.model.Subject;
import com.target.targetreadyresultsservice.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/subjects/v1")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    public SubjectController() {
    }

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }
    private static final Logger log = LoggerFactory.getLogger(SubjectController.class);
    @PostMapping("/subject")
    public ResponseEntity<Subject> setSubjectDetails(@RequestBody @Valid Subject subject) {
        try {
            Subject s = subjectService.addSubject(subject);
            return new ResponseEntity<>(s, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity("Error occurred "+e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/subject")
    public ResponseEntity<Subject> getAllSubjects() {
        try {
            List<Subject> allSubjects = subjectService.getSubjects();
            return new ResponseEntity(allSubjects, HttpStatus.OK);
        }
        catch (Exception e) {
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Error occurred "+e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/subject/{subjectCode}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable("subjectCode") String subjectCode) {
        try {
            Optional<Subject> subjectInfo=subjectService.getSubjectById(subjectCode);
            return subjectInfo.map(
                    subject -> new ResponseEntity<>(subject, HttpStatus.OK)
            ).orElseGet(
                    () -> new ResponseEntity("Could not find the subject with code : "+subjectCode, HttpStatus.NOT_FOUND)
            );

        } catch (Exception e) {
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Error occurred "+e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }


    @PutMapping("/subject/{subjectCode}")
    public ResponseEntity<Subject> updateSubjectById(@PathVariable("subjectCode") String subjectCode,@RequestBody Subject subject)  {
        try {
            Subject s = subjectService.updateSubject(subjectCode, subject);
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Error occurred while updating "+e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/subject/{subjectCode}")
    public ResponseEntity<String> deleteSubjectById(@PathVariable(value = "subjectCode") String subjectCode) {
        try {
            String s = subjectService.deleteSubject(subjectCode);
            return new ResponseEntity<>(s+ " Successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            log.info("exception occurred {}", e.getMessage());
       return new ResponseEntity<>("Error occurred while deleting "+e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }
    @DeleteMapping("/")
    public boolean deleteAllSubjects() {
        subjectService.deleteAll();
        return true;
    }
    @GetMapping("/search")
    public ResponseEntity<Subject> searchSubjects(@RequestParam(required = false) String subjectName)  {
        try {
            List<Subject> filterSub= new ArrayList<>();
            if(subjectName.isBlank()){
                filterSub = subjectService.getSubjects();
            }
            else{
                filterSub=subjectService.searchSubjectsByFilters(subjectName);
            }
            if(filterSub.isEmpty()) {
               return new ResponseEntity("Could not find any subject with name : "+subjectName, HttpStatus.NOT_FOUND);
            }
           else {
                return new ResponseEntity(filterSub, HttpStatus.OK);
            }
        }
        catch (Exception e) {
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Error occurred while searching, "+e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }
}
