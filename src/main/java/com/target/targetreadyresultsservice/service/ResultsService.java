package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.model.*;
import com.target.targetreadyresultsservice.repository.ResultsRepository;
import com.target.targetreadyresultsservice.repository.ScheduleRepository;
import com.target.targetreadyresultsservice.repository.StudentRepository;
import com.target.targetreadyresultsservice.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.List;

@Service
public class ResultsService{
    private final ResultsRepository resultsRepository;
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;
    private final ClassService classService;
    private final SubjectRepository subjectRepository;

    @Autowired
    public ResultsService(ResultsRepository resultsRepository, StudentRepository studentRepository, ScheduleRepository scheduleRepository, ScheduleService scheduleService, ClassService classService, SubjectRepository subjectRepository) {
        this.resultsRepository = resultsRepository;
        this.studentRepository = studentRepository;
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
        this.classService = classService;
        this.subjectRepository = subjectRepository;
    }

    public List<Results> getClassresult(String className, String acyear) {
        if (className.isBlank()) {
            throw new BlankValueException("Please enter a Class Name");
        }
        if (acyear.isBlank()) {
            throw new BlankValueException("Please enter an Academic Year");
        }
        String classCode = "";
        List<ClassDto> classDtoList = classService.getAllClasses();
        if (classDtoList.isEmpty()) {
            throw new NotFoundException(("No classes found!"));
        }
        for (ClassDto classDto : classDtoList) {
            if (classDto.getName().equals(className)) {
                classCode = classDto.getCode();
                break;
            }
        }
        if (classCode.isBlank()) {
            throw new InvalidValueException("Invalid Class Provided. Please enter a valid Class");
        }
        List<Schedule> scheduleList = scheduleService.getScheduleByClass(classCode);
        List<Results> classResultList = null;
        for (Schedule sc : scheduleList) {
            if (sc.getYear().equals(acyear)) {
                String schedule = sc.getScheduleCode();
                classResultList.addAll(resultsRepository.findALlByscheduleCode(schedule));
            }

        }
        if (classResultList.isEmpty()) {
            throw new NotFoundException("Results not Found");
        }
    return classResultList;
    }


    public Results addNewResult(Results result) {

        //check if student exists
        Student student = studentRepository.findById(result.getStudentId()).orElse(null);
        if(student==null){
            throw new NotFoundException("Action failed! Student not found");
        }

        //check if schedule exists
        Schedule schedule = scheduleRepository.findById(result.getScheduleCode()).orElse(null);
        if(schedule==null){
            throw new NotFoundException("Action failed! Schedule not found");
        }

        //check if mark list is empty
        List<Marks> marksList = result.getMarksList();
        if(marksList.isEmpty()){
            throw new BlankValueException("Action failed! Please enter marks for each subject");
        }

        for (Marks m : marksList) {

            Subject sub = subjectRepository.findById(m.getSubjectCode()).orElse(null);
            if(sub == null){
                throw new InvalidValueException("Enter a valid subject");
            }
        }

        //get the subject codes for all the subjects found in the schedule
        List<SubjectSchedule> subjectSchedule = schedule.getSubjectSchedule();
        List<String> subjectList = new ArrayList<>();
        for (SubjectSchedule s : subjectSchedule) {
            subjectList.add(s.getSubjectCode());
        }
        if(schedule.getScheduleType().equals("Test")){
            for (Marks m : marksList) {
                if(m.getExternalMarks()!=0){
                    m.setExternalMarks(0);
                }
                subjectList.remove(m.getSubjectCode());
            }
        }
        else{
            for (Marks m : marksList) {
                if(m.getInternalMarks()!=0) {
                    m.setInternalMarks(0);
                }
                subjectList.remove(m.getSubjectCode());
            }
        }
        if(!subjectList.isEmpty()){
            throw new BlankValueException("Action failed! \n" +
                    "Please provide marks for all the subjects in the schedule");
        }
        result.setResultsCode(addResultCode(result.getStudentId()));
        resultsRepository.save(result);
        return result;
    }

    private String addResultCode(String studentId) {
        String month = LocalDate.now().getMonth().toString();
        String year = Integer.toString(LocalDate.now().getYear());
        return "R"+studentId+month+year;
    }

    public Optional<Results> updateResult(String resultCode, Results results) {
        Results r = resultsRepository.findById(resultCode).orElse(null);
        if(r==null){
            throw new NotFoundException("Update failed! Result for this student is not found");
        }
        r.setStudentId(results.getStudentId());
        r.setScheduleCode(results.getScheduleCode());
        r.setMarksList(results.getMarksList());
        resultsRepository.save(r);
        return Optional.of(r);
    }

    public void deleteResult(String resultCode) {
        Results result = resultsRepository.findById(resultCode).orElse(null);
        if(result == null){
            throw new NotFoundException("Deletion Failed. No such Result!");

        }
        resultsRepository.delete(result);
    }
}