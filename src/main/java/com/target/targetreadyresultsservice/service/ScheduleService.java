package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.model.Schedule;
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


    public void addNewSchedule(Schedule schedule) {
        String code = addScheduleCode(schedule);
        schedule.setScheduleCode(code);
        scheduleRepository.save(schedule);
    }

    public String addScheduleCode(Schedule schedule) {
        long num = scheduleRepository.count();
        if(num==0){
            if(schedule.getScheduleType().contains("Test")){
                return "T1";
            }
            else{
                return "E1";
            }
        }
        else{
            if(schedule.getScheduleType().contains("Test")){
                return setTestCode();
            }
            else{
                return setExamCode();
            }
        }
    }

    public String setExamCode() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        int num=1;
        for (Schedule s: scheduleList) {
            if(s.getScheduleType().contains("Exam")){
                num++;
            }
        }
        return "E"+ Integer.toString(num);
    }

    public String setTestCode() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        int num=1;
        for (Schedule s: scheduleList) {
            if(s.getScheduleType().contains("Test")){
                num++;
            }
        }
        return "T"+ Integer.toString(num);
    }

    public String deleteSchedule(String scheduleCode) {
        Schedule schedule = scheduleRepository.findById(scheduleCode).orElse(null);
        if(schedule == null){
            return null;
        }
        scheduleRepository.delete(schedule);
        return "Deleted";
    }

    public Optional<Schedule> updateSchedule(String scheduleCode, Schedule schedule) {
        Schedule sc = scheduleRepository.findById(scheduleCode).orElse(null);
        if(sc==null){
            return Optional.empty();
        }
        sc.setClassCode(schedule.getClassCode());
        sc.setSubjectSchedule(schedule.getSubjectSchedule());
        sc.setScheduleType(schedule.getScheduleType());
        sc.setScheduleStatus(schedule.getScheduleStatus());
        scheduleRepository.save(sc);
        return Optional.of(sc);
    }
}
