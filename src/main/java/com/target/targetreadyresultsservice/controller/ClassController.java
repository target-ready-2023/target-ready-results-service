package com.target.targetreadyresultsservice.controller;


import com.target.targetreadyresultsservice.model.ClassLevel;
import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.service.ClassService;
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
    public List<ClassLevel> getClassDetails(){
       return classService.getAllClasses();
    }

    @GetMapping("/classes/class")
    public ResponseEntity<ClassLevel> getClassDetailsById(@RequestParam("classCode") String code){
        try {
            Optional<ClassLevel> classInfo = classService.getClassLevelById(code);
            return classInfo.map(
                    classInfo1 -> new ResponseEntity<>(classInfo1, HttpStatus.OK)
            ).orElseGet(
                    () -> new ResponseEntity<>(HttpStatus.NOT_FOUND)
            );
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/class")
    public ResponseEntity<String> saveClassDetails(@RequestBody ClassLevel classLevel){
        ClassLevel classInfo= classService.setClassLevelInfo(classLevel);
        return new ResponseEntity("class code: "+ classInfo.getCode(),HttpStatus.CREATED);
    }

    @PutMapping("/class")
    public ResponseEntity<String> UpdateClassDetails(@RequestParam("classCode") String code,
                                                     @RequestBody ClassLevel classLevel){
        classService.updateClassLevelInfo(code,classLevel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/class")
    public ResponseEntity<String> deleteClassDetails(@RequestParam("classCode") String code){
        classService.deleteClassLevelInfo(code);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/class/search")
    public List<ClassLevel> SearchClass(@RequestParam(required = false) String classCode,
                                        @RequestParam(required = false) String className){
        return classService.getClassLeveBySearch(classCode,className);

    }
}
