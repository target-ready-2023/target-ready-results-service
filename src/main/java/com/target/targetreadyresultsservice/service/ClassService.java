package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.controller.ClassController;
import com.target.targetreadyresultsservice.model.ClassLevel;
import com.target.targetreadyresultsservice.repository.ClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassService {
    @Autowired
    private final ClassRepository classRepository;

    @Autowired
    private SubjectService subjectService;
    @Autowired
    MongoTemplate mongoTemplate;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(ClassController.class);

    public List<ClassDto> getAllClasses(){
        log.info("Fetching all Class details from db");
        List<ClassLevel> classLevels=classRepository.findAll();
        List<ClassDto> classes = new ArrayList<>();
        for (ClassLevel level:classLevels) {
            List<String> subjects =subjectService.getSubjectsGivenClassCode(level.getCode());
            ClassDto classInfo = new ClassDto(level.getCode(), level.getName(),subjects);
            classes.add(classInfo);
        }
        return classes;
    }

    public ClassDto getClassLevelById(String code){
        ClassLevel classLevels = classRepository.findById(code)
                .orElseThrow(() -> new RuntimeException());
        if(classLevels==null){
            log.info("No class found with the class code {} in the db",code);
            return null;
        }
        else{
            List<String> subjects =subjectService.getSubjectsGivenClassCode(code);
            ClassDto classInfo = new ClassDto(classLevels.getCode(),classLevels.getName(),subjects);
            log.info("Fetching class details with class code {} from db", code);
            return classInfo;
        }
    }


    public ClassLevel setClassLevelInfo(ClassLevel classLevel){
            log.info("storing class {} into db", classLevel);
            String id;
            if((classLevel.getName()).length() <=2) {
                id = "C" + classLevel.getName();
            }
            else{
                id = "C" + (classLevel.getName()).substring(0,2);
            }
            classLevel.setCode(id);
            return classRepository.save(classLevel);
    }

    public void updateClassLevelInfo(String code,ClassLevel classLevel) {
        ClassLevel isClass=classRepository.findById(code)
                .orElseThrow(() -> new RuntimeException());
        isClass.setName(classLevel.getName());
        log.info("Updating class info with class code {} in th db",code);
        classRepository.save(isClass);
    }

    public void deleteClassLevelInfo(String code) {
        ClassLevel isClass=classRepository.findById(code)
                .orElseThrow(() -> new RuntimeException());
        classRepository.deleteById(code);
    }

    public ClassDto getClassLeveByName(String className) {
        try {
            List<ClassLevel> classLevels = classRepository.findAll();
            for (ClassLevel level : classLevels) {
                if ((level.getName()).equals(className)) {
                    List<String> subjects = subjectService.getSubjectsGivenClassCode(level.getCode());
                    ClassDto classInfo = new ClassDto(level.getCode(), level.getName(), subjects);
                    return classInfo;
                }
            }
        }catch(Exception e){
            throw new RuntimeException();
        }
        return null;
    }
}
