package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.ConstantConfig.DateTimeConfig;
import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.Exception.NullValueException;
import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.model.Subject;
import com.target.targetreadyresultsservice.model.SubjectSchedule;
import com.target.targetreadyresultsservice.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private final ScheduleRepository scheduleRepository;
    @Autowired
    private final ClassService classService;
    @Autowired
    private final SubjectService subjectService;

    public ScheduleService(ScheduleRepository scheduleRepository, ClassService classService, SubjectService subjectService) {
        this.scheduleRepository = scheduleRepository;
        this.classService = classService;
        this.subjectService = subjectService;
    }

    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    //get all schedule
    public List<Schedule> findAll(){
        log.info("All schedules found successfully");
        return scheduleRepository.findAll();
    }

    // get active Schedules by class by acYear
    public List<Schedule> getActiveSchedule(String classCode, String acYear){
        if(classCode.isBlank()){
            log.info("No class code provided - Throws BlankValueException");
            throw new BlankValueException("Please provide a class code");
        }
        if(acYear.isBlank()){
            log.info("No acYear code provided - Throws BlankValueException");
            throw new BlankValueException("Please provide an academic year");
        }
        List<Schedule> scheduleList = scheduleRepository.findByclassCode(classCode);
        List<Schedule> activeList = new ArrayList<>();
        for (Schedule s : scheduleList) {
            if(s.getYear().equals(acYear)){
                activeList.add(s);
            }
        }
        activeList.removeIf(s -> !s.getScheduleStatus());
        if(activeList.isEmpty()){
            log.info("Exception occurred - activeList is empty and throws NullValueException");
            throw new NullValueException("This class does not have any active schedules");
        }
        log.info("Active schedules found successfully - {}",activeList);
        return activeList;
    }

    //get all schedule by class
    public List<Schedule> getScheduleByClass(String classCode, String acYear) {
        List<Schedule> scheduleList = scheduleRepository.findByclassCode(classCode);
        if(scheduleList.isEmpty()){
            log.info("Exception occurred - scheduleList is empty and throws NotFoundException");
            throw new NotFoundException("No schedules found for the given class");
        }
        List<Schedule> schedulesByYear = new ArrayList<>();
        for (Schedule s :scheduleList) {
            if(s.getYear().equals(acYear)){
                schedulesByYear.add(s);
            }
        }if(schedulesByYear.isEmpty()){
            log.info("Exception occurred - schedulesByYear is empty and throws NotFoundException");
            throw new NotFoundException("No schedules found for the given class in the given academic year");
        }
        else{
            log.info("List of schedules found successfully as - {}",schedulesByYear);
            return schedulesByYear;
        }
    }

    //get schedule by id
    public Schedule getScheduleDetails(String scheduleCode) {
        Schedule sc = scheduleRepository.findById(scheduleCode).orElse(null);
        if(sc==null) {
            log.info("Exception occurred - sc is null and throws NotFoundException");
            throw new NotFoundException("Schedule match not found!");
        }
        else {
            log.info("schedule found successfully as - {}",sc);
            return sc;
        }
    }

    //add new schedule
    public Schedule addNewSchedule(Schedule schedule) {
        // exceptionChecks() performs all the initial exception checks
        //applicable for post and put
        exceptionChecks(schedule);
        log.info("All exceptions were cleared successfully to add new schedule");
        String code = addScheduleCode(schedule);
        log.info("Schedule code set as - {}",code);
        schedule.setScheduleCode(code);
        schedule.setYear(findScheduleYear(schedule.getSubjectSchedule()));
        scheduleRepository.save(schedule);
        log.info("Schedule added successfully");
        return schedule;
    }

    private String findScheduleYear(List<SubjectSchedule> subjectSchedule) {
        LocalDate date = subjectSchedule.get(0).getDate();
        String year = String.valueOf(date.getYear());

        //an academic year is from 1st June of a year to 31st March of the next year
        log.info("Month before acYear start - {}",DateTimeConfig.MONTH_BEFORE_YEAR_START);
        log.info("Day before acYEar start - {}",DateTimeConfig.DAY_BEFORE_YEAR_START);
        log.info("Month after acYear end - {}",DateTimeConfig.MONTH_AFTER_YEAR_END);
        log.info("Day after acYear end - {}",DateTimeConfig.DAY_AFTER_YEAR_END);

        if(date.isAfter(LocalDate.of(Integer.parseInt(year), DateTimeConfig.MONTH_BEFORE_YEAR_START,DateTimeConfig.DAY_BEFORE_YEAR_START)) &&
        date.isBefore(LocalDate.of(Integer.parseInt(year)+1, DateTimeConfig.MONTH_AFTER_YEAR_END, DateTimeConfig.DAY_AFTER_YEAR_END))){
            return year + "-" + Integer.toString(Integer.parseInt(year)+1);
        }
        if(date.isAfter(LocalDate.of(Integer.parseInt(year)-1, DateTimeConfig.MONTH_BEFORE_YEAR_START, DateTimeConfig.DAY_BEFORE_YEAR_START)) &&
                date.isBefore(LocalDate.of(Integer.parseInt(year), DateTimeConfig.MONTH_AFTER_YEAR_END, DateTimeConfig.DAY_AFTER_YEAR_END))){
            return Integer.parseInt(year)-1 +"-"+year;
        }
        log.info("Invalid date for exam provided. Cannot find the academic year");
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

        if(schedule.getScheduleType().equalsIgnoreCase("test")){
            //schedule code for tests starts with T
            return "T"+schedule.getClassCode()+day+month+year;
        }
        else if(schedule.getScheduleType().equalsIgnoreCase("exam")){
            //schedule code for exams starts with E
            return "E"+schedule.getClassCode()+day+month+year;
        }
        else{
            //schedule code for final exam starts with FE
            return "FE"+schedule.getClassCode()+day+month+year;
        }
    }

    //delete a schedule
    public Schedule deleteSchedule(String scheduleCode) {
        Schedule schedule = scheduleRepository.findById(scheduleCode).orElse(null);
        if(schedule == null){
            log.info("Schedule not found and throws NotFoundException");
            throw new NotFoundException("Deletion Failed !! Schedule not Found");
        }
        log.info("Schedule deleted successfully");
        scheduleRepository.delete(schedule);
        return schedule;
    }

    //update a schedule
    public Optional<Schedule> updateSchedule(String scheduleCode, Schedule schedule) {
        Schedule sc = scheduleRepository.findById(scheduleCode).orElse(null);
        if(sc==null){
            log.info("Schedule not found and throws NotFoundException");
            throw new NotFoundException("Update Failed ! Cannot find that Schedule.");
        }
        exceptionChecks(schedule);
        log.info("All exceptions were cleared successfully to update a schedule");
        sc.setClassCode(schedule.getClassCode());
        sc.setSubjectSchedule(schedule.getSubjectSchedule());
        sc.setScheduleType(schedule.getScheduleType());
        sc.setScheduleName(schedule.getScheduleName());
        sc.setYear(findScheduleYear(schedule.getSubjectSchedule()));
        sc.setScheduleStatus(schedule.getScheduleStatus());
        scheduleRepository.save(sc);
        log.info("Schedule updated successfully");
        return Optional.of(sc);
    }

    //exception checks for post and put
    public void exceptionChecks(Schedule schedule){
        if(schedule.getClassCode()==null){
            log.info("class code is null. Throws NullValueException");
            throw new NullValueException("Please enter a class code");
        }
        if(schedule.getClassCode().isBlank() || schedule.getClassCode().isEmpty()){
            log.info("No class code provided");
            throw new BlankValueException("Class code cannot be blank");
        }
        ClassDto classDto = classService.getClassLevelById(schedule.getClassCode());
        if(classDto==null){
            log.info("Class not found. Throws NotFoundException");
            throw new NotFoundException("Class not found. Action failed!");
        }
        if(schedule.getScheduleType().isBlank() || schedule.getScheduleType().isEmpty() || schedule.getScheduleType() == null){
            log.info("No schedule type provided");
            throw new BlankValueException("Schedule Type cannot be blank");
        }
        if(schedule.getScheduleName().isBlank() ||schedule.getScheduleName().isEmpty() || schedule.getScheduleName() == null){
            log.info("No schedule name provided");
            throw new BlankValueException("Schedule Name cannot be blank");
        }
        if(schedule.getSubjectSchedule().isEmpty() || schedule.getSubjectSchedule()==null){
            log.info("No subject schedule provided");
            throw new BlankValueException("Provide at least one subject schedule");
        }
        List<SubjectSchedule> SubjectList = schedule.getSubjectSchedule();
        for (SubjectSchedule s:
                SubjectList) {
            if(s.getSubjectCode().isBlank() || s.getSubjectCode().isEmpty()){
                log.info("No subjectCode provided");
                throw new BlankValueException("Schedule Code cannot be blank");
            }
            Subject subject = subjectService.getSubjectById(s.getSubjectCode()).orElse(null);
            if(subject==null){
                log.info("Subject not found for - {}",s.getSubjectCode());
                throw new NotFoundException("Subject not found! Please enter a valid subject");
            }
            if(!subject.getClassCode().equals(schedule.getClassCode())){
                log.info("subject provided is not taught to the given class");
                throw new InvalidValueException("Action failed! Selected subject not applicable for the given class");
            }
            if(s.getDate() == null){
                log.info("No date provided");
                throw new BlankValueException("Please enter a date");
            }
            if(s.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                log.info("Date provided is a sunday");
                throw new InvalidValueException("This day is a Sunday. Please enter a working Day");
            }
            if(s.getTime() == null){
                log.info("Time is not provided");
                throw new BlankValueException("Please enter a time");
            }

            log.info("Day start time hour - {}",DateTimeConfig.HOUR_BEFORE_DAY_START);
            log.info("Day start time minute - {}",DateTimeConfig.MINUTE_BEFORE_DAY_START);
            log.info("Day end time hour - {}",DateTimeConfig.HOUR_AFTER_DAY_END);
            log.info("Day end time minute - {}",DateTimeConfig.MINUTE_AFTER_DAY_END);

            if(!(s.getTime().isAfter(LocalTime.of(DateTimeConfig.HOUR_BEFORE_DAY_START,
                    DateTimeConfig.MINUTE_BEFORE_DAY_START)) &&
                    s.getTime().isBefore(LocalTime.of(DateTimeConfig.HOUR_AFTER_DAY_END,
                            DateTimeConfig.MINUTE_AFTER_DAY_END)))){
                log.info("Time provided is not a working time for school");
                throw new InvalidValueException("Please enter a time between "+
                        LocalTime.of(DateTimeConfig.HOUR_BEFORE_DAY_START,
                                DateTimeConfig.MINUTE_BEFORE_DAY_START) +
                        " and "+ LocalTime.of(DateTimeConfig.HOUR_AFTER_DAY_END,
                        DateTimeConfig.MINUTE_AFTER_DAY_END));
            }
        }
    }

    //get a schedule from schedule name, class name and academic year
    //needed for results-service
    public Schedule getScheduleForResult(String scheduleName, String className, String acYear) {
        if(scheduleName.isBlank()){
            log.info("No schedule name provided. Throws BlankValueException");
            throw new BlankValueException("Please enter a schedule name");
        }
        if(className.isBlank()){
            log.info("No class name provided. Throws BlankValueException");
            throw new BlankValueException("Please enter a class name");
        }
        if(acYear.isBlank()){
            log.info("No academic year provided. Throws BlankValueException");
            throw new BlankValueException("Please enter an academic year");
        }
        String classCode = "";
        List<ClassDto> classDtoList = classService.getAllClasses();
        if(classDtoList.isEmpty()){
            log.info("Class provide is not found. Throws NotFoundException");
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
            log.info("No schedules found");
            throw new NotFoundException("This class does not have any schedules");
        }
        for (Schedule sc : scheduleList) {
            if(sc.getScheduleName().equals(scheduleName) && sc.getYear().equals(acYear)){
                return sc;
            }
        }
        throw new NotFoundException("Schedule not found!");
    }

    public List<String> getScheduleNamesForClass(String className, String acYear) {
        List<ClassDto> classDto = classService.getClassLeveByName(className);
        log.info("Class found as - {}",classDto);
        if(classDto.isEmpty()){
            log.info("No class found with class name - {}",className);
            throw new NotFoundException("No class found with the given class name");
        }
        List<Schedule> scheduleList = getScheduleByClass(classDto.get(0).getCode(),acYear);
        if(scheduleList.isEmpty()){
            throw new NotFoundException("No schedules found");
        }
        log.info("Schedule list for {} found as - {}",className,scheduleList);
        List<String> scheduleNameList = new ArrayList<>();
        for (Schedule s :scheduleList) {
            if(s.getYear().equals(acYear)){
                scheduleNameList.add(s.getScheduleName());
            }
        }
        if(scheduleNameList.isEmpty()){
            log.info("No schedules found");
            throw new NotFoundException("No schedules found");
        }
        log.info("Schedule name list found as - {}",scheduleNameList);
        return scheduleNameList;
    }

    //get all schedules by acYear
    public List<Schedule> getScheduleByYear(String acYear) {
        if(acYear == null) {
            log.info("No academic year provided - Throws NullValueException");
            throw new NullValueException("Please provide an academic year");
        }
        if(acYear.isBlank()){
            log.info("No academic year provided - Throws BlankValueException");
            throw new BlankValueException("Please provide an academic year");
        }
        List<Schedule> scheduleList = scheduleRepository.findByyear(acYear);
        if(scheduleList.isEmpty()){
            log.info("No schedules found for the given academic year - Throws NotFoundException");
            throw new NotFoundException("No schedules found for the given academic year");
        }
        log.info("Schedule list for the academic year - {} found as - {}",acYear,scheduleList);
        return scheduleList;
    }

    //get list of acYears for a class
    public List<String> getScheduleAcYearsForClass(String classCode) {
        if(classCode.isBlank()){
            log.info("Class code is blank. throws BlankValueException");
            throw new BlankValueException("Please provide a class code");
        }
        List<String> acYears = new ArrayList<>();
        List<Schedule> scheduleList = scheduleRepository.findByclassCode(classCode);
        if(scheduleList.isEmpty()){
            log.info("No schedules found for - {}",classCode);
            throw new NotFoundException("No schedules found for this class");
        }
        log.info("scheduleList found as  - {}",scheduleList);
        for (Schedule s : scheduleList) {
            if(!acYears.contains(s.getYear())){
                acYears.add(s.getYear());
            }
        }
        if(acYears.isEmpty()){
            log.info("acYears not found");
            throw new NotFoundException("No schedules found");
        }
        log.info("acYears found as - {}",acYears);
        return acYears;
    }
}
