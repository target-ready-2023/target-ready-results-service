package com.target.targetreadyresultsservice.controller;


import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.model.ClassLevel;
import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.service.ClassService;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/results/v1")
public class ClassController {
    private ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping("/classes")
    public ResponseEntity<String> getClassDetails(){
        try {
            List<ClassDto> classes = classService.getAllClasses();
            return new ResponseEntity("successfully fetched\n"+classes, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity("Error occurred during fetch",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/classes/class")
    public ResponseEntity<ClassLevel> getClassDetailsById(@RequestParam("classCode") String code){
        try {
            ClassLevel classInfo = classService.getClassLevelById(code);
            if(classInfo!=null) {
                return new ResponseEntity<>(classInfo, HttpStatus.OK);
            }
            else{
                return new ResponseEntity("Data not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity("Error occurred during fetch",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/class")
    public ResponseEntity<String> saveClassDetails(@RequestBody ClassLevel classLevel){
        try {
            ClassLevel classInfo = classService.setClassLevelInfo(classLevel);
            return new ResponseEntity<>("successfully saved", HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>("Error occurred during save", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/class")
    public ResponseEntity<String> UpdateClassDetails(@RequestParam("classCode") String code,
                                                     @RequestBody ClassLevel classLevel){
        try{
        classService.updateClassLevelInfo(code,classLevel);
        return new ResponseEntity<>("Successfully updated",HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>("Error occurred during update",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/class")
    public ResponseEntity<String> deleteClassDetails(@RequestParam("classCode") String code){
        try {
            classService.deleteClassLevelInfo(code);
            return new ResponseEntity<>("Successfully deleted",HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>("Error occurred during delete",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/class/search")
    public ResponseEntity<String> SearchClassBYName(@RequestParam(value = "className", required = false) String className){
        try {
            List<ClassLevel> classInfo = classService.getClassLeveByName(className);
            if (classInfo.isEmpty()) {
                return new ResponseEntity<>("No data found", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity(classInfo, HttpStatus.FOUND);
            }
        } catch(Exception e){
            return new ResponseEntity<>("Error during search", HttpStatus.EXPECTATION_FAILED);
        }

    }
}
