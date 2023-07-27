package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.model.Results;
import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.repository.ResultsRepository;
import com.target.targetreadyresultsservice.repository.ScheduleRepository;
import com.target.targetreadyresultsservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ResultsService{
    private final ResultsRepository resultsRepository;
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ResultsService(ResultsRepository resultsRepository, StudentRepository studentRepository, ScheduleRepository scheduleRepository) {
        this.resultsRepository = resultsRepository;
        this.studentRepository = studentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public void addNewResult(Results result) {
        Student student = studentRepository.findById(result.getStudentId()).orElse(null);
        if(student==null){
            throw new NotFoundException("Action failed! Student not found");
        }
        Schedule schedule = scheduleRepository.findById(result.getScheduleCode()).orElse(null);
        if(schedule==null){
            throw new NotFoundException("Action failed! Schedule not found");
        }
        result.setResultsCode(addResultCode(result.getStudentId()));
        resultsRepository.save(result);
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

}