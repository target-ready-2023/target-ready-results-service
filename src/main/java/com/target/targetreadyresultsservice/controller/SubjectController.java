package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.Exception.SubjectNotFoundException;
import com.target.targetreadyresultsservice.model.Subject;
import com.target.targetreadyresultsservice.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<String> setSubjectDetails(@RequestBody Subject subject) {
        try {
            subjectService.addSubject(subject);
            return new ResponseEntity<>("Successfully added", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/subjects")
    public ResponseEntity<String> getAllSubjects() {
        try {
            List<Subject> allSubjects = subjectService.getSubjects();
            return new ResponseEntity<>("Successfully fetched\n"+allSubjects, HttpStatus.OK);
        }
        catch (Exception e)
        {
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>("Error occurred", HttpStatus.EXPECTATION_FAILED);
        }



    }

    @GetMapping("/subject/{subjectCode}")
    public ResponseEntity<String> getSubjectById(@PathVariable("subjectCode") String subjectCode) {
        try {
            Optional<Subject> subjectInfo=subjectService.getSubjectById(subjectCode);
            return subjectInfo.map(
                    subject -> new ResponseEntity<>("Found : " + subject.toString(), HttpStatus.OK)
            ).orElseGet(
                    () -> new ResponseEntity<>("Could not find the subject with code : "+subjectCode, HttpStatus.NOT_FOUND)
            );

        } catch (Exception e) {
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>("Error occurred",HttpStatus.EXPECTATION_FAILED);
        }

    }


    @PutMapping("/subject/{subjectCode}")
    public ResponseEntity<String> updateSubjectById(@RequestBody Subject subject, @PathVariable("subjectCode") String subjectCode)  {
        try {
            subjectService.updateSubject(subjectCode, subject);
            return new ResponseEntity<>("Successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>("Error occurred while updating",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/subject/{subjectCode}")
    public ResponseEntity<String> deleteSubjectById(@PathVariable(value = "subjectCode") String subjectCode) {
        try {
            subjectService.deleteSubject(subjectCode);
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            log.info("exception occurred {}", e.getMessage());
       return new ResponseEntity<>("Error occurred while deleting",HttpStatus.EXPECTATION_FAILED);
        }
    }
    @DeleteMapping("/")
    public boolean deleteAllSubjects()
    {
        subjectService.deleteAll();
        return true;
    }
    @GetMapping("/search")
    public ResponseEntity<String> searchSubjects(@RequestParam(required = false) String subjectName)  {
        try {
            List<Subject> filterSub=subjectService.searchSubjectsByFilters(subjectName);
            if(!filterSub.isEmpty()) {
                return new ResponseEntity<>("Successfully fetched\n" + filterSub, HttpStatus.OK);
            }
            else
            {
               return new ResponseEntity<>("Could not find any subject with name : "+subjectName, HttpStatus.NOT_FOUND);
            }

        }
        catch (Exception e)
        {
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>("Error occurred while searching",HttpStatus.EXPECTATION_FAILED);
        }
    }

}
