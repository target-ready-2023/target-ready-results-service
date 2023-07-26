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

    //get schedule by class
    public List<Schedule> getScheduleByClass(String classCode) {
        List<Schedule> scheduleList = scheduleRepository.findByclassCode(classCode);
        if(scheduleList.isEmpty()){
            throw new NullValueException("No schedules found for the given class");
        }
        else{
            return scheduleList;
        }
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
    public Schedule addNewSchedule(Schedule schedule) {
        exceptionChecks(schedule);
        String code = addScheduleCode(schedule);
        schedule.setScheduleCode(code);
        schedule.setYear(findScheduleYear(schedule.getSubjectSchedule()));
        scheduleRepository.save(schedule);
        return schedule;
    }

    private String findScheduleYear(List<SubjectSchedule> subjectSchedule) {
        LocalDate date = subjectSchedule.get(0).getDate();
        String year = String.valueOf(date.getYear());
        return year;
    }

    //create schedule code T/E-classCode-dateOfE/T
    public String addScheduleCode(Schedule schedule) {
        List<SubjectSchedule> subjectScheduleList = schedule.getSubjectSchedule();
        LocalDate date = subjectScheduleList.get(0).getDate();
        String day = String.valueOf(date.getDayOfMonth());
        String month = String.valueOf(date.getMonth());
        String year = String.valueOf(date.getYear());

        if(schedule.getScheduleType().equals("Test")){
                return "T"+schedule.getClassCode()+day+month+year;
        }
        else{
                return "E"+schedule.getClassCode()+day+month+year;
        }
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
        exceptionChecks(schedule);
        sc.setClassCode(schedule.getClassCode());
        sc.setSubjectSchedule(schedule.getSubjectSchedule());
        sc.setScheduleType(schedule.getScheduleType());
        sc.setScheduleName(schedule.getScheduleName());
        sc.setYear(findScheduleYear(schedule.getSubjectSchedule()));
        sc.setScheduleStatus(schedule.getScheduleStatus());
        scheduleRepository.save(sc);
        return Optional.of(sc);
    }

    //exception checks for post and put
    public void exceptionChecks(Schedule schedule){
        if(schedule.getClassCode()==null){
            throw new NullValueException("Please enter a class code");
        }
        if(schedule.getClassCode().isBlank() || schedule.getClassCode().isEmpty() ){
            throw new BlankValueException("Class code cannot be blank");
        }
        if(schedule.getScheduleType().isBlank() || schedule.getScheduleType().isEmpty() || schedule.getScheduleType() == null){
            throw new BlankValueException("Schedule Type cannot be blank");
        }
        if(schedule.getScheduleName().isBlank() ||schedule.getScheduleName().isEmpty() || schedule.getScheduleName() == null){
            throw new BlankValueException("Schedule Name cannot be blank");
        }
        if(schedule.getSubjectSchedule().isEmpty() || schedule.getSubjectSchedule()==null){
            throw new BlankValueException("Provide at least one subject schedule");
        }
        List<SubjectSchedule> SubjectList = schedule.getSubjectSchedule();
        for (SubjectSchedule s:
                SubjectList) {
            if(s.getSubjectCode().isBlank() || s.getSubjectCode().isEmpty()){
                throw new BlankValueException("Schedule Code cannot be blank");
            }
            if(s.getDate() == null){
                throw new BlankValueException("Please enter a date");
            }
            if(s.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                throw new InvalidValueException("This day is a Sunday. Please enter a working Day");
            }
            if(s.getTime() == null){
                throw new BlankValueException("Please enter a time");
            }
            if(!(s.getTime().isAfter(LocalTime.of(9,0)) && s.getTime().isBefore(LocalTime.of(16,0)) )){
                throw new InvalidValueException("Please enter a time between 9AM and 4PM");
            }
        }
    }
}
