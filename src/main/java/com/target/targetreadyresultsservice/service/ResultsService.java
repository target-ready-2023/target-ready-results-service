package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.model.*;
import com.target.targetreadyresultsservice.repository.ResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@Service
public class ResultsService{
    private final ResultsRepository resultsRepository;
    private final StudentService studentService;
    private final ScheduleService scheduleService;
    private final ClassService classService;
    private final SubjectService subjectService;

    @Autowired
    public ResultsService(ResultsRepository resultsRepository,
                          StudentService studentService, ScheduleService scheduleService,
                          ClassService classService,SubjectService subjectService) {
        this.resultsRepository = resultsRepository;
        this.studentService = studentService;
        this.scheduleService = scheduleService;
        this.classService = classService;
        this.subjectService = subjectService;
    }

    //get results for all tests and exams for a class in an academic year
    public List<Results> getClassResult(String className, String acYear) {
        if (className.isBlank()) {
            throw new BlankValueException("Please enter a Class Name");
        }
        if (acYear.isBlank()) {
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
        List<Results> classResultList = new ArrayList<>();
        for (Schedule sc : scheduleList) {
            if (sc.getYear().equals(acYear)) {
                String schedule = sc.getScheduleCode();
                classResultList.addAll(resultsRepository.findAllByscheduleCode(schedule));
            }

        }
        if (classResultList.isEmpty()) {
            throw new NotFoundException("Results not Found");
        }
    return classResultList;
    }

    //add a new result for a student
    public Results addNewResult(Results result) {

        exceptionChecks(result);

        Schedule schedule = scheduleService.getScheduleDetails(result.getScheduleCode());

        List<Marks> marksList = result.getMarksList();

        Student student = studentService.getStudentInfo(result.getStudentId()).orElse(null);

        //get the subject codes for all the subjects found in the schedule
        List<SubjectSchedule> subjectSchedule = schedule.getSubjectSchedule();
        List<String> subjectList = new ArrayList<>();
        for (SubjectSchedule s : subjectSchedule) {
            subjectList.add(s.getSubjectCode());
        }
        if(schedule.getScheduleType().equalsIgnoreCase("test")){
            for (Marks m : marksList) {
                if(m.getExternalMarks()!=0){
                    m.setExternalMarks(0);
                }
                subjectList.remove(m.getSubjectCode());
            }
        }
        else if(schedule.getScheduleType().equalsIgnoreCase("exam")){
                for (Marks m : marksList) {
                    if (m.getInternalMarks() != 0) {
                        m.setInternalMarks(0);
                    }
                    subjectList.remove(m.getSubjectCode());
                }
            }
        else if(!schedule.getScheduleType().equalsIgnoreCase("final exam")){
            throw new InvalidValueException("The schedule type should be 'test', 'exam' or 'final exam'");
        }
        else {
            for (Marks m : marksList) {
                Subject sub = subjectService.getSubjectById(m.getSubjectCode()).orElse(null);

                m.setInternalMarks(getAverageForSubject(student.getStudentId(),schedule.getYear(),sub.getSubjectCode()));
                subjectList.remove(m.getSubjectCode());
            }
        }
        if(!subjectList.isEmpty()){
            throw new BlankValueException("Action failed! \n" +
                    "Please provide marks for all the subjects in the schedule");
        }
        result.setResultsCode(addResultCode(result.getStudentId(),schedule.getSubjectSchedule()));
        resultsRepository.save(result);
        return result;
    }

    //create result code
    private String addResultCode(String studentId, List<SubjectSchedule> subjectScheduleList) {
        LocalDate date = subjectScheduleList.get(0).getDate();
        String day = String.valueOf(date.getDayOfMonth());
        String month = String.valueOf(date.getMonth());
        String year = String.valueOf(date.getYear());
        return "R"+studentId+day+month+year;
    }

    //update result for a student
    public Optional<Results> updateResult(String resultCode, Results results) {
        Results r = resultsRepository.findById(resultCode).orElse(null);
        if(r==null){
            throw new NotFoundException("Update failed! Result for this student is not found");
        }
        exceptionChecks(r);

        Schedule schedule = scheduleService.getScheduleDetails(results.getScheduleCode());

        List<Marks> marksList = results.getMarksList();

        Student student = studentService.getStudentInfo(results.getStudentId()).orElse(null);

        //get the subject codes for all the subjects found in the schedule
        List<SubjectSchedule> subjectSchedule = schedule.getSubjectSchedule();
        List<String> subjectList = new ArrayList<>();
        for (SubjectSchedule s : subjectSchedule) {
            subjectList.add(s.getSubjectCode());
        }
        if(schedule.getScheduleType().equalsIgnoreCase("test")){
            for (Marks m : marksList) {
                if(m.getExternalMarks()!=0){
                    m.setExternalMarks(0);
                }
                subjectList.remove(m.getSubjectCode());
            }
        }
        else if(schedule.getScheduleType().equalsIgnoreCase("exam")){
            for (Marks m : marksList) {
                if (m.getInternalMarks() != 0) {
                    m.setInternalMarks(0);
                }
                subjectList.remove(m.getSubjectCode());
            }
        }
        else if(!schedule.getScheduleType().equalsIgnoreCase("final exam")){
            throw new InvalidValueException("The schedule type should be 'test', 'exam' or 'final exam'");
        }
        else {
            for (Marks m : marksList) {
                Subject sub = subjectService.getSubjectById(m.getSubjectCode()).orElse(null);

                m.setInternalMarks(getAverageForSubject(student.getStudentId(),schedule.getYear(),sub.getSubjectCode()));
                subjectList.remove(m.getSubjectCode());
            }
        }
        if(!subjectList.isEmpty()){
            throw new BlankValueException("Action failed! \n" +
                    "Please provide marks for all the subjects in the schedule");
        }

        r.setStudentId(results.getStudentId());
        r.setScheduleCode(results.getScheduleCode());
        r.setMarksList(results.getMarksList());
        resultsRepository.save(r);
        return Optional.of(r);
    }

    //exception checks for post and put
    public void exceptionChecks(Results r){

        //check if student exists
        Student student = studentService.getStudentInfo(r.getStudentId()).orElse(null);
        if(student==null){
            throw new NotFoundException("Action failed! Student not found");
        }

        //check if schedule exists
        Schedule schedule = scheduleService.getScheduleDetails(r.getScheduleCode());
        if(schedule==null){
            throw new NotFoundException("Action failed! Schedule not found");
        }

        //check if mark list is empty
        List<Marks> marksList = r.getMarksList();
        if(marksList.isEmpty()){
            throw new BlankValueException("Action failed! Please enter marks for each subject");
        }
        for (Marks m : marksList) {
            Subject sub = subjectService.getSubjectById(m.getSubjectCode()).orElse(null);
            if(sub == null){
                throw new InvalidValueException("Enter a valid subject");
            }
            if(schedule.getScheduleType().equalsIgnoreCase("test")){
                if(sub.getMaxTestMarks()<m.getInternalMarks()){
                    throw new InvalidValueException("Action failed! Test marks for "
                            +sub.getSubjectName()+" cannot be "+ m.getInternalMarks() +"\n" +
                            "NOTE: The maximum exam marks for "+sub.getSubjectName()+" is "+sub.getMaxTestMarks());
                }
            }
            if(schedule.getScheduleType().equalsIgnoreCase("exam") ||
                    schedule.getScheduleType().equalsIgnoreCase("final exam")){
                if(sub.getMaxExamMarks()<m.getExternalMarks()){
                    throw new InvalidValueException("Action failed! Exam marks for "
                            +sub.getSubjectName()+" cannot be "+ m.getExternalMarks() +"\n" +
                            "NOTE: The maximum exam marks for "+sub.getSubjectName()+" is "+sub.getMaxExamMarks());
                }
            }
        }
    }

    //delete a result
    public void deleteResult(String resultCode) {
        Results result = resultsRepository.findById(resultCode).orElse(null);
        if(result == null){
            throw new NotFoundException("Deletion Failed. No such Result!");

        }
        resultsRepository.delete(result);
    }

    //get average internals in a subject for final exam using student id and academic year
    public float getAverageForSubject(String studentId, String acYear, String subjectCode){

        Student student = studentService.getStudentInfo(studentId).orElse(null);
        if(student==null){
            throw new NotFoundException("Student not found");
        }

        //get results by class name and academic year
        ClassDto classOfStudent = classService.getClassLevelById(student.getClassCode());
        if(classOfStudent==null){
            throw new NotFoundException("Class not found");
        }
        List<Results> resultsList = getClassResult(classOfStudent.getName(),acYear);
        float avgInternals = 0;
        int count = 0;
        for (Results r : resultsList) {
            Schedule thisSchedule = scheduleService.getScheduleDetails(r.getScheduleCode());
            if(thisSchedule.getScheduleType().equals("Test") &&
                    (r.getStudentId().equals(student.getStudentId()))) {
                List<Marks> marksList = r.getMarksList();
                for (Marks m : marksList) {
                    if(m.getSubjectCode().equals(subjectCode)){
                        avgInternals+=m.getInternalMarks();
                        count++;
                        break;
                    }
                }
            }
        }
        if(count==0){
            throw new InvalidValueException("Internal marks cannot be found for the given values");
        }
        avgInternals=avgInternals/count;
        return avgInternals;
    }

    //get result for a given schedule in a class for an academic year
    public List<Results> getClassTestResults(String className, String acYear, String scName) {
        if (className.isBlank()) {
            throw new BlankValueException("Please enter a Class Name");
        }
        if (acYear.isBlank()) {
            throw new BlankValueException("Please enter an Academic Year");
        }
        if (scName.isBlank()) {
            throw new BlankValueException("Please enter an Schedule Name");
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
        List<Results> classTestResultList = new ArrayList<>();
        for (Schedule sc : scheduleList) {
            if (sc.getYear().equals(acYear) && sc.getScheduleName().equals(scName)) {
                String schedule = sc.getScheduleCode();
                 classTestResultList =resultsRepository.findAllByscheduleCode(schedule);
                
            }
        }
        if (classTestResultList.isEmpty()) {
            throw new NotFoundException("Results not Found");
        }
        return classTestResultList;
    }

    //get all test and exam results for one student
    public List<Results> getStudentResult(String studentId, String acYear) {

        Student student = studentService.getStudentInfo(studentId).orElse(null);
        if(student==null){
            throw new NotFoundException("Student not found");
        }

        List<Results> results = resultsRepository.findAllBystudentId(student.getStudentId());
        List<Results> resultsList = new ArrayList<>();
        if(results.isEmpty()){
            throw new NotFoundException("No results found for student - "+ student.getName());
        }
        for (Results r :results) {
            Schedule schedule = scheduleService.getScheduleDetails(r.getScheduleCode());
            if(schedule==null){
                throw new NotFoundException("Schedule not found");
            }
            if(schedule.getYear().equals(acYear)){
                resultsList.add(r);
            }
        }
        return resultsList;
    }

    public double getResultPercentage(String studentId, String acYear) {
        return 23.8;
    }
}