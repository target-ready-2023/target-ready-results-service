package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.Exception.NullValueException;
import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.model.SubjectSchedule;
import com.target.targetreadyresultsservice.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
        if(activeList.isEmpty()){
            throw new NullValueException("This class does not have any active schedules");
        }
        return activeList;
    }

    //Get schedule by id
    public Schedule getScheduleDetails(String scheduleCode) {
        Schedule sc = scheduleRepository.findById(scheduleCode).orElse(null);
        if(sc==null)
            throw new NotFoundException("Schedule match not found!");
        else
            return sc;
    }
    //add new schedule
    public void addNewSchedule(Schedule schedule) {

        if(schedule.getClassCode().isBlank() || schedule.getClassCode().isEmpty() ){
            throw new BlankValueException("Class code cannot be blank");
        }
        if(schedule.getScheduleType().isBlank() || schedule.getScheduleType().isEmpty()){
            throw new BlankValueException("Schedule Type cannot be blank");
        }
        if(schedule.getScheduleName().isBlank() ||schedule.getScheduleName().isEmpty()  ){
            throw new BlankValueException("Schedule Name cannot be blank");
        }
        if(schedule.getSubjectSchedule().isEmpty()){
            throw new BlankValueException("Provide at least one subject schedule");
        }
        List<SubjectSchedule> SubjectList = schedule.getSubjectSchedule();
        for (SubjectSchedule s:
                SubjectList) {
            if(s.getSubjectCode().isBlank() || s.getSubjectCode().isEmpty()){
                throw new BlankValueException("Schedule Code cannot be blank");
            }
            if(s.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                throw new InvalidValueException("This day is a Sunday. Please enter a working Day");
            }
            if(!(s.getTime().isAfter(LocalTime.of(9,0)) && s.getTime().isBefore(LocalTime.of(16,0)))){
                throw new InvalidValueException("Please enter a time between 9AM and 4PM");
            }

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
            throw new NotFoundException("Deletion Failed !! Schedule not Found");
        }
        scheduleRepository.delete(schedule);
        return "Deleted";
    }

    //update a schedule
    public Optional<Schedule> updateSchedule(String scheduleCode, Schedule schedule) {
        Schedule sc = scheduleRepository.findById(scheduleCode).orElse(null);
        if(sc==null){
            throw new NotFoundException("Update Failed ! Cannot find that Schedule.");
        }
        if(schedule.getClassCode().isBlank() || schedule.getClassCode().isEmpty() ){
            throw new BlankValueException("Class code cannot be blank");
        }
        if(schedule.getScheduleType().isBlank() || schedule.getScheduleType().isEmpty()){
            throw new BlankValueException("Schedule Type cannot be blank");
        }
        if(schedule.getScheduleName().isBlank() ||schedule.getScheduleName().isEmpty()  ){
            throw new BlankValueException("Schedule Name cannot be blank");
        }
        if(schedule.getSubjectSchedule().isEmpty()){
            throw new BlankValueException("Provide at least one subject schedule");
        }
        List<SubjectSchedule> SubjectList = schedule.getSubjectSchedule();
        for (SubjectSchedule s:
                SubjectList) {
            if(s.getSubjectCode().isBlank() || s.getSubjectCode().isEmpty()){
                throw new BlankValueException("Schedule Code cannot be blank");
            }
            if(s.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                throw new InvalidValueException("This day is a Sunday. Please enter a working Day");
            }
            if(!(s.getTime().isAfter(LocalTime.of(9,0)) && s.getTime().isBefore(LocalTime.of(16,0)) )){
                throw new InvalidValueException("Please enter a time between 9AM and 4PM");
            }

        }
        sc.setClassCode(schedule.getClassCode());
        sc.setSubjectSchedule(schedule.getSubjectSchedule());
        sc.setScheduleType(schedule.getScheduleType());
        sc.setScheduleStatus(schedule.getScheduleStatus());
        scheduleRepository.save(sc);
        return Optional.of(sc);
    }
}
