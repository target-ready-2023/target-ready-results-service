package com.target.targetreadyresultsservice.service;


//import com.target.targetreadyresultsservice.controller.ResultsController;
import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.controller.SubjectController;
import com.target.targetreadyresultsservice.model.Subject;
import com.target.targetreadyresultsservice.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

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
    private static final Logger log = LoggerFactory.getLogger(SubjectController.class);

    public List<String> getSubjectsGivenClassCode(String code) {
        List<String> subNames = new ArrayList<>();
        List<Subject> subInfo = subjectRepository.findByClassCode(code);
        for(Subject s: subInfo){
                String sub =s.getSubjectName();
                subNames.add(sub);
        }
        Collections.sort(subNames);
        return subNames;
    }

    public Subject addSubject(Subject subject)  {
        String code="S_"+subject.getSubjectName().substring(0,2)+subject.getClassCode();
        subject.setSubjectCode(code);
        Optional<Subject> savedSub=subjectRepository.findById(subject.getSubjectCode());
        if(savedSub.isPresent())
        {   log.info("Subject already present");
            throw new RuntimeException("Subject already present");
        }
        else {
            return subjectRepository.save(subject);
        }
    }
    public List<Subject> getSubjects()
    {
        log.info("fetching all the subjects");
        List<Subject> subjects=subjectRepository.findAll();
        if(subjects.isEmpty())
        {
            throw new RuntimeException("No subjects found");
        }
        return subjects;
    }

    public Optional<Subject> getSubjectById(String subjectCode) {
        log.info("getting subject by ID");
        Optional<Subject> sub=subjectRepository.findById(subjectCode);
        if(sub.isEmpty())
        {
            throw new RuntimeException("No subject found for "+subjectCode);
        }
        return sub;
    }

    public Subject updateSubject(String subjectCode, Subject subject) {
        Optional<Subject> subject1= subjectRepository.findById(subjectCode);
        if(subject1.isPresent()) {
            log.info("updating subject");
            Subject sub = subject1.get();
            sub.setSubjectName(subject.getSubjectName());
            sub.setCredits(subject.getCredits());
            sub.setClassCode(subject.getClassCode());
            sub.setMaxExamMarks(subject.getMaxExamMarks());
            sub.setMaxTestMarks(subject.getMaxTestMarks());
            subjectRepository.save(sub);
            return sub;
        }
        else
        {
            log.info("Could not find the subject with code"+subjectCode);
            throw new RuntimeException("Could not find the subject with code"+subjectCode);
        }
    }
    public String deleteSubject(String subjectCode)
    {
        Optional<Subject> subject=subjectRepository.findById(subjectCode);
        if(subject.isPresent()) {
            log.info("Deleting subject with code "+subjectCode);
            subjectRepository.deleteById(subjectCode);
            return subjectCode;
           }
        else {
            log.info("Could not find the subject with code"+subjectCode);
             throw new RuntimeException("Could not find the subject with code"+subjectCode);
             }
    }

    public void deleteAll() {
        subjectRepository.deleteAll();
    }

    public List<Subject> searchSubjectsByFilters(String subjectName)
    {
        log.info("getting subjects with subject name");
            List<Subject> subjects=subjectRepository.findBySubjectNameIgnoreCase(subjectName);
              if(subjects.isEmpty())
                {
                    log.info("Could not find any subject with name"+subjectName);
                    throw new RuntimeException("No subject present with name "+subjectName);
                }
           return subjects;
    }

    public List<Subject> getSubjectsObjectGivenClassCode(String code) {
        List<Subject> subInfo = subjectRepository.findByClassCode(code);
        log.info("fetching all the subjects with class Code {}",code);
        if(subInfo.isEmpty())
        {
            return null;
        }
        Collections.sort(subInfo, new Comparator<Subject>() {
            @Override
            public int compare(Subject o1, Subject o2) {
                String s1= o1.getSubjectName();
                String s2 = o2.getSubjectName();

                return s1.compareToIgnoreCase(s2);
            }
        });
        return subInfo;
    }
}


