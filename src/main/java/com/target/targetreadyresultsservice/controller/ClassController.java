package com.target.targetreadyresultsservice.controller;


import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.model.ClassLevel;
import com.target.targetreadyresultsservice.service.ClassService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/classes/v1")
public class ClassController {
    private ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping("/classes")
    public ResponseEntity<List<ClassDto>> getClassDetails() {
        try {
            List<ClassDto> classes = classService.getAllClasses();
            if (classes != null && classes.toString() !="") {
                return new ResponseEntity<>(classes, HttpStatus.OK);
            }
            else{
                return new ResponseEntity("Data not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity("Error occurred during fetch", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("classes/{classCode}")
    public ResponseEntity<ClassDto> getClassDetailsById(@PathVariable("classCode") String code){
        try {
            ClassDto classInfo = classService.getClassLevelById(code);
            if(classInfo!=null && classInfo.toString()!="") {
                return new ResponseEntity<>(classInfo, HttpStatus.OK);
            }
            else{
                return new ResponseEntity("Data not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/classes")
    public ResponseEntity<String> saveClassDetails(@RequestBody @Valid ClassLevel classLevel){
        try {
            ClassLevel classInfo = classService.setClassLevelInfo(classLevel);
            return new ResponseEntity<>("successfully saved", HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>("Error occurred during save", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/classes/{classCode}")
    public ResponseEntity<String> UpdateClassDetails(@PathVariable("classCode") String code,
                                                     @RequestBody ClassLevel classLevel){
        try{
            ClassLevel classLevel1 = classService.updateClassLevelInfo(code,classLevel);
        return new ResponseEntity<>("Successfully updated",HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>("Error occurred during update",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/classes/{classCode}")
    public ResponseEntity<String> deleteClassDetails(@PathVariable("classCode") String code){
        try {
            classService.deleteClassLevelInfo(code);
            return new ResponseEntity<>("Successfully deleted",HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/classes/search")
    public ResponseEntity<List<ClassDto>> SearchClassBYName(@RequestParam(required = false) String classCode,
                                                            @RequestParam(required = false) String className){
        try {
            List<ClassDto> classInfo = classService.getClassLeveByName(classCode,className);
            if (classInfo==null || classInfo.isEmpty()) {
                return new ResponseEntity("No data found", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity(classInfo, HttpStatus.OK);
            }
        } catch(Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

    }
}
