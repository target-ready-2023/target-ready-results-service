package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;


@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    //get all schedule
    public List<Schedule> findAll(){
        return scheduleRepository.findAll();
    }
    // get active Schedules
    public List<Schedule> getactiveSchedule(String classCode){
    List<Schedule> activeList = scheduleRepository.findByclassCode(classCode);
        activeList.removeIf(s -> !s.getScheduleStatus());
        return activeList;
    }

    //Get schedule by id
    public Schedule getScheduleDetails(String scheduleCode) {
        return scheduleRepository.findById(scheduleCode).orElse(null);
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
        return "E"+ num;
    }

    public String setTestCode() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        int num=1;
        for (Schedule s: scheduleList) {
            if(s.getScheduleType().contains("Test")){
                num++;
            }
        }
        return "T"+ num;
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
