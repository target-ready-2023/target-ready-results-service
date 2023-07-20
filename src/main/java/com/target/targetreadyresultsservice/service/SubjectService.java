package com.target.targetreadyresultsservice.service;


import com.target.targetreadyresultsservice.controller.ResultsController;
import com.target.targetreadyresultsservice.model.Subject;
import com.target.targetreadyresultsservice.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }
    public SubjectService() {
    }
    private static final Logger log = LoggerFactory.getLogger(ResultsController.class);

    public List<String> getSubjectsGivenClassCode(String code) {
        List<String> subNames = new ArrayList<>();
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where("classCode").is(code));
        if (!criteria.isEmpty())
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        List<Subject> subInfo = mongoTemplate.find(query,Subject.class);
        for(Subject s: subInfo){
                subNames.add(s.getSubjectName());
        }
        return subNames;
    }

    public  String addSubject(Subject subject) throws Exception {
        String code="S-"+subject.getSubjectName().substring(0,2)+subject.getClassCode();
        subject.setSubjectCode(code);
        subjectRepository.save(subject);
        return "Subject added successfully";
    }
    public List<Subject> getSubjects()
    {
        log.info("fetching all the subjects");
        return subjectRepository.findAll();
    }

    public Optional<Subject> getSubjectById(String subjectCode) {
        return subjectRepository.findById(subjectCode);
    }

    public void updateSubject(String subjectCode, Subject subject) {
        Optional<Subject> subject1= subjectRepository.findById(subjectCode);
        if(subject1.isPresent()) {
            Subject sub = subject1.get();
            sub.setSubjectName(subject.getSubjectName());
            sub.setCredits(subject.getCredits());
            sub.setClassCode(subject.getClassCode());
            subjectRepository.save(sub);
        }
        else
        {
            log.info("Could not find the subject with code"+subjectCode);
            throw new RuntimeException();
        }
    }
    public void deleteSubject(String subjectCode)
    {
        Optional<Subject> subject=subjectRepository.findById(subjectCode);
        if(subject.isPresent()) {
            log.info("Deleting subject with code "+subjectCode);
            subjectRepository.deleteById(subjectCode);
           }
        else {
            log.info("Could not find the subject with code"+subjectCode);
             throw new RuntimeException();
             }
    }

    public void deleteAll() {
        subjectRepository.deleteAll();
    }

    public List<Subject> searchSubjectsByFilters(String subjectName)  {
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();
        if (subjectName != null && !subjectName.isEmpty())
            criteria.add(Criteria.where("subjectName").is(subjectName));
        if (!criteria.isEmpty())
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
       return mongoTemplate.find(query,Subject.class);


    }
}


