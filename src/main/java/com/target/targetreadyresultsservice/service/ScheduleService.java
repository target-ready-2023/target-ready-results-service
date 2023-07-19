package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.model.SubjectSchedule;
import com.target.targetreadyresultsservice.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    // get active Schedules by class
    public List<Schedule> getactiveSchedule(String classCode){
    List<Schedule> activeList = scheduleRepository.findByclassCode(classCode);
        activeList.removeIf(s -> !s.getScheduleStatus());
        return activeList;
    }

    //Get schedule by id
    public Schedule getScheduleDetails(String scheduleCode) {
        return scheduleRepository.findById(scheduleCode).orElse(null);
    }

    //add new schedule
    public void addNewSchedule(Schedule schedule) {
        if(schedule.getClassCode().isBlank()){
            throw new BlankValueException("Class code cannot be blank");
        }
        String code = addScheduleCode(schedule);
        schedule.setScheduleCode(code);
        scheduleRepository.save(schedule);
    }

    //create schedule code T/E-classCode-dateOfE/T
    public String addScheduleCode(Schedule schedule) {
            if(schedule.getScheduleType().equals("Test")){
                return setTestCode(schedule);
            }
            else{
                return setExamCode();
            }
    }

    //create exam schedule code
    public String setExamCode() {
        return "E";
    }

    //create test schedule code
    public String setTestCode(Schedule schedule) {
        List<SubjectSchedule> subjectScheduleList = schedule.getSubjectSchedule();
        LocalDate date = subjectScheduleList.get(0).getDate();
        String month = String.valueOf(date.getMonth());
        String year = String.valueOf(date.getYear());
        return "T"+schedule.getClassCode()+month+year;
    }

    //delete a schedule
    public String deleteSchedule(String scheduleCode) {
        Schedule schedule = scheduleRepository.findById(scheduleCode).orElse(null);
        if(schedule == null){
            return null;
        }
        scheduleRepository.delete(schedule);
        return "Deleted";
    }

    //update a schedule
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
