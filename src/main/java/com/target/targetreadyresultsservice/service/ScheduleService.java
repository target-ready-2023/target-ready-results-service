package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.Exception.NullValueException;
import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.model.Subject;
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
    private final ClassService classService;
    private final SubjectService subjectService;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, ClassService classService, SubjectService subjectService) {
        this.scheduleRepository = scheduleRepository;
        this.classService = classService;
        this.subjectService = subjectService;
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

        //an academic year is from 1st June of a year to 31st March of the next year

        if(date.isAfter(LocalDate.of(Integer.parseInt(year),5,31)) &&
        date.isBefore(LocalDate.of(Integer.parseInt(year)+1,4,1))){
            return year + "-" + Integer.toString(Integer.parseInt(year)+1);
        }
        if(date.isAfter(LocalDate.of(Integer.parseInt(year)-1, 5,31)) &&
                date.isBefore(LocalDate.of(Integer.parseInt(year),4,1))){
            return Integer.parseInt(year)-1 +"-"+year;
        }
        throw new InvalidValueException("Action failed! Please provide a valid date within the academic year \n" +
                "NOTE: An academic year is from 1st June of a year to 31st March of the next year");
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
        ClassDto classDto = classService.getClassLevelById(schedule.getClassCode());
        if(classDto==null){
            throw new NotFoundException("Class not found. Action failed!");
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
            Subject subject = subjectService.getSubjectById(s.getSubjectCode()).orElse(null);
            if(subject==null){
                throw new NotFoundException("Subject not found! Please enter a valid subject");
            }
            if(!subject.getClassCode().equals(schedule.getClassCode())){
                throw new InvalidValueException("Action failed! Selected subject not applicable for the given class");
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

    //get a schedule from schedule name, class name and academic year
    //needed for results-service
    public Schedule getScheduleForResult(String scheduleName, String className, String acYear) {
        if(scheduleName.isBlank()){
            throw new BlankValueException("Please enter a schedule name");
        }
        if(className.isBlank()){
            throw new BlankValueException("Please enter a class name");
        }
        if(acYear.isBlank()){
            throw new BlankValueException("Please enter an academic year");
        }
        String classCode = "";
        List<ClassDto> classDtoList = classService.getAllClasses();
        if(classDtoList.isEmpty()){
            throw new NotFoundException("Class not found. Please enter an existing class name");
        }
        for (ClassDto classDto :classDtoList) {
            if(classDto.getName().equals(className)){
                classCode = classDto.getCode();
                break;
            }
        }
        if(classCode.isBlank()){
            throw new InvalidValueException("Invalid class provided. Please enter a valid class");
        }
        List<Schedule> scheduleList = scheduleRepository.findByclassCode(classCode);
        if(scheduleList.isEmpty()){
            throw new NotFoundException("This class does not have any schedules");
        }
        for (Schedule sc : scheduleList) {
            if(sc.getScheduleName().equals(scheduleName) && sc.getYear().equals(acYear)){
                return sc;
            }
        }
        throw new NotFoundException("Schedule not found!");
    }
}
