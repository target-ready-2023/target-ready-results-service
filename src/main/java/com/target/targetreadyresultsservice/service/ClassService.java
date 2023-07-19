package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Exception.ClassLevelNotFoundException;
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
import java.util.Optional;

@Service
public class ClassService {
    @Autowired
    private final ClassRepository classRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(ClassController.class);

    public List<ClassLevel> getAllClasses(){
        log.info("Fetching all Class details from db");
        return classRepository.findAll();
    }

    public Optional<ClassLevel> getClassLevelById(String code){
        Optional<ClassLevel> classInfo = classRepository.findById(code);
        if(classInfo==null){
            log.info("No class found with the class code {} in the db",code);
            return null;
        }
        else{
            log.info("Fetching class details with class code {} from db", code);
            return classInfo;
        }
    }


    public ClassLevel setClassLevelInfo(ClassLevel classLevel){
        try {
            log.info("storing class {} into db", classLevel);
            String id="C"+classLevel.getName();
            classLevel.setCode(id);
            return classRepository.save(classLevel);
        } catch(Exception e){
            log.info("error while storing class {} into db", e.toString());
            return null;
        }
    }

    public void updateClassLevelInfo(String code,ClassLevel classLevel) {
        ClassLevel isClass=classRepository.findById(code)
                .orElseThrow(() -> new ClassLevelNotFoundException(code));
        isClass.setName(classLevel.getName());
        isClass.setSubjectCodes(classLevel.getSubjectCodes());
        log.info("Updating class info with class code {} in th db",code);
        classRepository.save(isClass);
    }

    public void deleteClassLevelInfo(String code) {
        Optional<ClassLevel> isClass=classRepository.findById(code);

        if(isClass.isPresent()){
            classRepository.deleteById(code);
            log.info("Deleted class with class code {} successfully",code);
        }
        else{
            log.info("There is no class with class code {}",code);
            throw new ClassLevelNotFoundException(code);
        }
    }

    public List<ClassLevel> getClassLeveBySearch(String classCode, String className) {
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();
        if (classCode != null && !classCode.isEmpty())
            criteria.add(Criteria.where("code").is(classCode));
        if (className != null && !className.isEmpty())
            criteria.add(Criteria.where("name").is(className));

        if (!criteria.isEmpty())
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        log.info("Searching class with class code {} or class name {} in the db",classCode,className);

        return mongoTemplate.find(query,ClassLevel.class);
    }
}
