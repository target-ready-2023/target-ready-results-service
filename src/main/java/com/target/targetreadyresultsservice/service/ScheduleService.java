package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Optional<Schedule> getScheduleDetails(String scheduleCode) {
        return scheduleRepository.findById(scheduleCode);
    }

    public void addNewScheduleTest(Schedule schedule){
        long num = scheduleRepository.count();
        if(num==0){
            if(schedule.getScheduleType().contains("Test")){
                schedule.setScheduleCode("T1");
            }
            else{
                schedule.setScheduleCode("E1");
            }
        }
        else{
            if(schedule.getScheduleType().contains("Test")){
                schedule.setScheduleCode("T"+num+1);
            }
            else{
                schedule.setScheduleCode("E"+num+1);
            }
        }

    }

    public void addNewSchedule(Schedule schedule) {
        if(schedule.getScheduleType().contains("Test")){
            schedule.setScheduleCode(setTestCode());
        }
        else{
            schedule.setScheduleCode(setExamCode());
        }
        scheduleRepository.save(schedule);
    }

    private String setExamCode() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        if(scheduleList.isEmpty()){
            return "E1";
        }
        int num=1;
        for (Schedule s: scheduleList) {
            if(s.getScheduleType().contains("Exam")){
                num++;
            }
        }
        return "E"+num;
    }

    private String setTestCode() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        if(scheduleList.isEmpty()){
            return "T1";
        }
        int num=1;
        for (Schedule s: scheduleList) {
            if(s.getScheduleType().contains("Test")){
                num++;
            }
        }
        return "T"+num;
    }
}
