package com.target.targetreadyresultsservice.controller;


import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.model.ClassLevel;
import com.target.targetreadyresultsservice.service.ClassService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/classes/v1")
public class ClassController {
    @Autowired
    private ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    private static final Logger log = LoggerFactory.getLogger(ClassController.class);

    @GetMapping("/classes")
    public ResponseEntity<List<ClassDto>> getClassDetails() {
        try {
            List<ClassDto> classes = classService.getAllClasses();
            if ((classes != null) && !(classes.toString().isEmpty())) {
                log.info("Classes found successfully as - {}",classes);
                return new ResponseEntity<>(classes, HttpStatus.OK);
            }
            else{
                log.info("No classes found");
                return new ResponseEntity("Data not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity("Error occurred during fetch", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("classes/{classCode}")
    public ResponseEntity<ClassDto> getClassDetailsById(@PathVariable("classCode") String code){
        try {
            ClassDto classInfo = classService.getClassLevelById(code);
            if(classInfo!=null && !(classInfo.toString().isEmpty())) {
                log.info("Class found as - {}",classInfo);
                return new ResponseEntity<>(classInfo, HttpStatus.OK);
            }
            else{
                log.info("Class not found");
                return new ResponseEntity("Data not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/classes")
    public ResponseEntity<String> saveClassDetails(@RequestBody @Valid ClassLevel classLevel){
        try {
            ClassLevel classInfo = classService.setClassLevelInfo(classLevel);
            log.info("New class is added successfully as - {}",classInfo);
            return new ResponseEntity<>("successfully saved", HttpStatus.CREATED);
        } catch(Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/classes/{classCode}")
    public ResponseEntity<String> UpdateClassDetails(@PathVariable("classCode") String code,
                                                     @RequestBody ClassLevel classLevel){
        try{
            ClassLevel classLevel1 = classService.updateClassLevelInfo(code,classLevel);
            log.info("class updated successfully as - {}",classLevel1);
            return new ResponseEntity<>("Successfully updated",HttpStatus.OK);
        } catch(Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity<>("Error occurred during update",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/classes/{classCode}")
    public ResponseEntity<String> deleteClassDetails(@PathVariable("classCode") String code){
        try {
            classService.deleteClassLevelInfo(code);
            log.info("Class deleted successfully");
            return new ResponseEntity<>("Successfully deleted",HttpStatus.OK);
        } catch(Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/classes/search")
    public ResponseEntity<List<ClassDto>> SearchClassBYName(@RequestParam(required = false) String className){
        try {
            List<ClassDto> classInfo = new ArrayList<>();
            if(className.isBlank()){
                classInfo=classService.getAllClasses();
            }
            else{
                 classInfo = classService.getClassLeveByName(className);
            }
            if (classInfo==null || classInfo.isEmpty()) {
                log.info("No data is found");
                return new ResponseEntity("No data found", HttpStatus.NOT_FOUND);
            } else {
                log.info("class found as - {}",classInfo);
                return new ResponseEntity(classInfo, HttpStatus.OK);
            }
        } catch(Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

    }
}
