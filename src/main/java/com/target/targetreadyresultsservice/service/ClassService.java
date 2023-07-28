package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.controller.ClassController;
import com.target.targetreadyresultsservice.model.ClassLevel;
import com.target.targetreadyresultsservice.repository.ClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassService {
    @Autowired
    private final ClassRepository classRepository;
    @Autowired
    private final SubjectService subjectService;

    public ClassService(ClassRepository classRepository, SubjectService subjectService) {
        this.classRepository = classRepository;
        this.subjectService= subjectService;
    }
    private static final Logger log = LoggerFactory.getLogger(ClassController.class);
    public List<ClassDto> getAllClasses(){
        log.info("Fetching all Class details from db");
        List<ClassLevel> classLevels=classRepository.findAll();
        if(!classLevels.isEmpty()) {
            List<ClassDto> classes = new ArrayList<>();
            for (ClassLevel level : classLevels) {
                List<String> subjects = subjectService.getSubjectsGivenClassCode(level.getCode());
                ClassDto classInfo = new ClassDto(level.getCode(), level.getName(), subjects);
                classes.add(classInfo);
            }
            return classes;
        }
        else{
            return null;
        }

    }

    public ClassDto getClassLevelById(String code){
        ClassLevel classLevels = classRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Class with given code "+code+" is not found"));

            List<String> subjects =subjectService.getSubjectsGivenClassCode(code);
            ClassDto classInfo = new ClassDto(classLevels.getCode(),classLevels.getName(),subjects);
            log.info("Fetching class details with class code {} from db", code);
            return classInfo;

    }


    public ClassLevel setClassLevelInfo(ClassLevel classLevel){
            log.info("storing class {} into db", classLevel);

            String id;
            if( classRepository.existsByName(classLevel.getName())){
                throw new RuntimeException("class already exists with the given className: "+classLevel.getName());
            }
            else if((classLevel.getName()).isBlank()){
                throw new RuntimeException("class name cannot be null or empty");
            }
            else{
                id = "C" + (classLevel.getName());
            }
            classLevel.setCode(id.toUpperCase());
            return classRepository.save(classLevel);
    }

    public ClassLevel updateClassLevelInfo(String code,ClassLevel classLevel) {
        ClassLevel isClass=classRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Class with given code "+code+" is not found"));
        isClass.setName(classLevel.getName());
        log.info("Updating class info with class code {} in th db",code);
        return classRepository.save(isClass);
    }

    public String deleteClassLevelInfo(String code) {
        boolean isClass=classRepository.existsById(code);
        if(isClass) {
           classRepository.deleteById(code);
           return "deleted successfully";
        }
        else throw new RuntimeException("Class with given code "+code+" is not found");
    }

    public List<ClassDto> getClassLeveByName(String className) {

        List<ClassLevel> classLevels = classRepository.findByNameIgnoreCase(className);

        if(!classLevels.isEmpty()) {
            List<ClassDto> classes = new ArrayList<>();
            for (ClassLevel level : classLevels) {
                List<String> subjects = subjectService.getSubjectsGivenClassCode(level.getCode());
                ClassDto classInfo = new ClassDto(level.getCode(), level.getName(), subjects);
                classes.add(classInfo);
            }
            return classes;
        }
        else{
                return null;
        }

    }
}