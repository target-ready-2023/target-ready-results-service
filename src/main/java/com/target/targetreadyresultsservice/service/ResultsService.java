package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.Dto.ResultsDto;
import com.target.targetreadyresultsservice.Dto.StudentDto;
import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.model.*;
import com.target.targetreadyresultsservice.repository.ResultsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

@Service
public class ResultsService{
    @Autowired
    private final ResultsRepository resultsRepository;
    @Autowired
    private final StudentService studentService;
    @Autowired
    private final ScheduleService scheduleService;
    @Autowired
    private final ClassService classService;
    @Autowired
    private final SubjectService subjectService;

    public ResultsService(ResultsRepository resultsRepository,
                          StudentService studentService, ScheduleService scheduleService,
                          ClassService classService,SubjectService subjectService) {
        this.resultsRepository = resultsRepository;
        this.studentService = studentService;
        this.scheduleService = scheduleService;
        this.classService = classService;
        this.subjectService = subjectService;
    }

    private static final Logger log = LoggerFactory.getLogger(ResultsService.class);

    //get results for all tests, exams and final exam for a class in an academic year
    public List<ResultsDto> getClassResult(String className, String acYear) {
        if (className.isBlank()) {
            log.info("Class name is not provided. Throws BlankValueException");
            throw new BlankValueException("Please enter a Class Name");
        }
        if (acYear.isBlank()) {
            log.info("Academic year not provided. Throws BlankValueException");
            throw new BlankValueException("Please enter an Academic Year");
        }
        String classCode = classService.getClassCodeFromName(className);
        if(classCode.isEmpty()){
            log.info("Class not found. Throws NotFoundException");
            throw new NotFoundException("Class not found");
        }
        List<Schedule> scheduleList = scheduleService.getScheduleByClass(classCode,acYear);
        if(scheduleList.isEmpty()){
            log.info("No schedules found for this class. Throws NotFoundException");
            throw new NotFoundException("No schedules found for this class.");
        }
        List<Results> classResultList = new ArrayList<>();
        for (Schedule sc : scheduleList) {
            if (sc.getYear().equals(acYear)) {
                String schedule = sc.getScheduleCode();
                classResultList.addAll(resultsRepository.findAllByscheduleCode(schedule));
            }
        }
        List<ResultsDto> classResultsDtoList = new ArrayList<>();
        for (Results r: classResultList) {
            Schedule s = scheduleService.getScheduleDetails(r.getScheduleCode());
            List<Double> aggregates= getAggregates(s.getScheduleType(),r.getMarksList());
            ResultsDto result = new ResultsDto(
                    r.getResultsCode(),
                    r.getStudentId(),
                    r.getScheduleCode(),
                    r.getMarksList(),
                    aggregates.get(0),
                    aggregates.get(1)
            );
            classResultsDtoList.add(result);
        }
        if (classResultList.isEmpty()) {
            log.info("No results found. Throws NotFoundException");
            throw new NotFoundException("Results not Found");
        }
        log.info("Class results found successfully as - {}",classResultList);
        return classResultsDtoList;
    }

    //add a new result for a student
    public Results addNewResult(Results result) {

        //exceptionChecks has the initial exception checks common to put and post
        exceptionChecks(result);
        log.info("Successfully cleared exception checks for adding new result");

        Schedule schedule = scheduleService.getScheduleDetails(result.getScheduleCode());

        List<Marks> marksList = result.getMarksList();

        Student student = studentService.getStudentInfo(result.getStudentId()).orElse(null);
        log.info("Student found as - {}. This is from addNewResult",student);

        ClassDto classLevel = classService.getClassLevelById(student.getClassCode());
        String className = classLevel.getName();
        log.info("The classname is found as - {}",className);

        //get the subject codes for all the subjects found in the schedule to check
        // if marks for all subjects are provided
        List<SubjectSchedule> subjectSchedule = schedule.getSubjectSchedule();
        List<String> subjectList = new ArrayList<>();
        for (SubjectSchedule s : subjectSchedule) {
            subjectList.add(s.getSubjectCode());
        }
        //for each subject, if the result is for a test, then the external marks is set as zero
        if(schedule.getScheduleType().equalsIgnoreCase("test")){
            for (Marks m : marksList) {
                if(m.getExternalMarks()!=0){
                    m.setExternalMarks(0);
                }
                subjectList.remove(m.getSubjectCode());
            }
        }
        //for each subject, if the result is for an exam (not final exam), then the internal marks is set as zero
        else if(schedule.getScheduleType().equalsIgnoreCase("exam")){
                for (Marks m : marksList) {
                    if (m.getInternalMarks() != 0) {
                        m.setInternalMarks(0);
                    }
                    subjectList.remove(m.getSubjectCode());
                }
            }
        else if(!schedule.getScheduleType().toLowerCase().contains("final")){
            log.info("The schedule type provided is not test, exam or final exam");
            throw new InvalidValueException("The schedule type should be 'test', 'exam' or 'final exam'");
        }
        //if schedule type is final exam, find average of test for each subject and set as internal marks
        else {
            for (Marks m : marksList) {
                Subject sub = subjectService.getSubjectById(m.getSubjectCode()).orElse(null);
                //exception check already done by the exceptionCheck function
                m.setInternalMarks(getAverageForSubject(student.getRollNumber(),className,schedule.getYear(),sub.getSubjectCode()));
                log.info("Internals set as - {} for - {}",m.getInternalMarks(),m.getSubjectCode());
                subjectList.remove(m.getSubjectCode());
            }
        }
        if(!subjectList.isEmpty()){
            log.info("Marks for all the subjects were not provided");
            throw new BlankValueException("Action failed! \n" +
                    "Please provide marks for all the subjects in the schedule");
        }
        result.setResultsCode(addResultCode(result.getStudentId(),schedule.getScheduleCode()));
        resultsRepository.save(result);
        log.info("Result added successfully");
        return result;
    }

    //create result code
    private String addResultCode(String studentId, String scheduleCode) {
        return "R"+studentId+scheduleCode;
    }

    //update result for a student
    public Optional<Results> updateResult(String resultCode, Results results) {
        Results r = resultsRepository.findById(resultCode).orElse(null);
        if(r==null){
            log.info("Result not found");
            throw new NotFoundException("Update failed! Result for this student is not found");
        }
        exceptionChecks(r);
        log.info("Successfully cleared all the initial exception checks to update results by exceptionChecks()");

        Schedule schedule = scheduleService.getScheduleDetails(results.getScheduleCode());

        List<Marks> marksList = results.getMarksList();

        Student student = studentService.getStudentInfo(results.getStudentId()).orElse(null);

        ClassDto classDto = classService.getClassLevelById(student.getClassCode());

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
        else if(!schedule.getScheduleType().toLowerCase().contains("final")){
            log.info("Invalid schedule type provided. Throws InvalidValueException");
            throw new InvalidValueException("The schedule type should be 'test', 'exam' or 'final exam'");
        }
        else {
            for (Marks m : marksList) {
                Subject sub = subjectService.getSubjectById(m.getSubjectCode()).orElse(null);

                m.setInternalMarks(getAverageForSubject(student.getRollNumber(),classDto.getName(),schedule.getYear(),sub.getSubjectCode()));
                subjectList.remove(m.getSubjectCode());
            }
        }
        if(!subjectList.isEmpty()){
            log.info("Marks for all the subjects in the schedule is not provided. Throws BlankValueException");
            throw new BlankValueException("Action failed! \n" +
                    "Please provide marks for all the subjects in the schedule");
        }

        r.setStudentId(results.getStudentId());
        r.setScheduleCode(results.getScheduleCode());
        r.setMarksList(results.getMarksList());
        resultsRepository.save(r);
        log.info("Result updated successfully as - {}",r);
        return Optional.of(r);
    }

    //exception checks common for post and put
    public void exceptionChecks(Results r){
        //check if student exists
        Student student = studentService.getStudentInfo(r.getStudentId()).orElse(null);
        if(student==null){
            log.info("Student not found for - {}",r.getStudentId());
            throw new NotFoundException("Action failed! Student not found");
        }
        log.info("Student found as - {}",student);

        //check if schedule exists
        Schedule schedule = scheduleService.getScheduleDetails(r.getScheduleCode());
        if(schedule==null){
            log.info("Schedule not found for - {}",r.getScheduleCode());
            throw new NotFoundException("Action failed! Schedule not found");
        }
        log.info("Schedule found as - {}",schedule);

        //check if mark list is empty
        List<Marks> marksList = r.getMarksList();
        if(marksList.isEmpty()){
            log.info("marksList is empty. Throws BlankValueException");
            throw new BlankValueException("Action failed! Please enter marks for each subject");
        }
        for (Marks m : marksList) {
            Subject sub = subjectService.getSubjectById(m.getSubjectCode()).orElse(null);
            if(sub == null){
                log.info("Subject - {} not found. Throws InvalidValueException",m.getSubjectCode());
                throw new InvalidValueException("Enter a valid subject");
            }
            if(schedule.getScheduleType().equalsIgnoreCase("test")){
                if(sub.getMaxTestMarks()<m.getInternalMarks()){
                    log.info("Marks provided is greater than the maximum test marks allowed " +
                            "for the subject - {}",sub.getSubjectCode());
                    throw new InvalidValueException("Action failed! Test marks for "
                            +sub.getSubjectName()+" cannot be "+ m.getInternalMarks() +"\n" +
                            "NOTE: The maximum exam marks for "+sub.getSubjectName()+" is "+sub.getMaxTestMarks());
                }
            }
            if(schedule.getScheduleType().equalsIgnoreCase("exam") ||
                    schedule.getScheduleType().toLowerCase().contains("final")){
                if(sub.getMaxExamMarks()<m.getExternalMarks()){
                    log.info("Marks provided is greater than the maximum exam marks allowed " +
                            "for the subject - {}",sub.getSubjectCode());
                    throw new InvalidValueException("Action failed! Exam marks for "
                            +sub.getSubjectName()+" cannot be "+ m.getExternalMarks() +"\n" +
                            "NOTE: The maximum exam marks for "+sub.getSubjectName()+" is "+sub.getMaxExamMarks());
                }
            }
        }
        log.info("Initial exception checks complete");
    }

    //delete a result
    public Results deleteResult(String resultCode) {
        Results result = resultsRepository.findById(resultCode).orElse(null);
        if(result == null){
            log.info("Result not found. Throws NotFoundException");
            throw new NotFoundException("Deletion Failed. No such Result!");
        }
        log.info("Result - {} - deleted successfully",result);
        resultsRepository.delete(result);
        return result;
    }

    //get average internals in a subject for final exam using student id and academic year
    public double getAverageForSubject(String rollNumber,String className, String acYear, String subjectCode){

        Student student = studentService.getStudentFromClassRollNo(className,rollNumber);
        log.info("Student found as - {}.This is from average",student);
        if(student==null){
            log.info("Student not found. Throws NotFoundException");
            throw new NotFoundException("Student not found");
        }
        //get results by class name and academic year
        ClassDto classOfStudent = classService.getClassLevelById(student.getClassCode());
        log.info("Class of student found as - {}",classOfStudent);
        if(classOfStudent==null){
            log.info("Class of the given student is not found");
            throw new NotFoundException("Class not found");
        }
        //get the list of all the results for a class in an academic year
        List<ResultsDto> resultsList = getClassResult(classOfStudent.getName(),acYear);
        double avgInternals = 0;
        //count is used to find the number of tests
        int count = 0;
        for (ResultsDto r : resultsList) {
            Schedule thisSchedule = scheduleService.getScheduleDetails(r.getScheduleCode());
            //add the internal marks for the subject if schedule type is test
            if(thisSchedule.getScheduleType().equalsIgnoreCase("Test") &&
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
            log.info("No test results were found");
            throw new InvalidValueException("Internal marks cannot be found for the given values");
        }
        avgInternals=avgInternals/count;
        log.info("Average found successfully as - {}",avgInternals);
        return avgInternals;
    }

    //get result for a given schedule in a class for an academic year
    public List<ResultsDto> getClassTestResults(String classCode, String acYear, String scCode) {
        if (classCode.isBlank()) {
            log.info("No class code provided. Throws BlankValueException");
            throw new BlankValueException("Please enter a Class code");
        }
        if (acYear.isBlank()) {
            log.info("No academic year provided. Throws BlankValueException");
            throw new BlankValueException("Please enter an Academic Year");
        }
        if (scCode.isBlank()) {
            log.info("No schedule code provided. Throws BlankValueException");
            throw new BlankValueException("Please enter a schedule code");
        }
        List<Schedule> scheduleList = scheduleService.getScheduleByClass(classCode,acYear);
        if(scheduleList.isEmpty()){
            log.info("No schedules found for this class. Throws NotFoundException");
            throw new NotFoundException("No schedules found for this class");
        }
        log.info("Schedule list found as - {}",scheduleList);
        List<Results> classTestResultList = new ArrayList<>();
        for (Schedule sc : scheduleList) {
            if (sc.getScheduleCode().equals(scCode)) {
               log.info("The schedule code found (inside loop) is - {}",sc);
                 classTestResultList.addAll(resultsRepository.findAllByscheduleCode(sc.getScheduleCode()));
            }
        }
        List<ResultsDto> classTestResultsDtoList = new ArrayList<>();
        for (Results r: classTestResultList) {
            Schedule s = scheduleService.getScheduleDetails(r.getScheduleCode());
            List<Double> aggregates= getAggregates(s.getScheduleType(),r.getMarksList());
            ResultsDto result = new ResultsDto(
                    r.getResultsCode(),
                    r.getStudentId(),
                    r.getScheduleCode(),
                    r.getMarksList(),
                    aggregates.get(0),
                    aggregates.get(1)
            );
            classTestResultsDtoList.add(result);
        }
        if (classTestResultsDtoList.isEmpty()) {
            log.info("Results not found for the class provided. Throws NotFoundException");
            throw new NotFoundException("Results not Found");
        }
        log.info("Results found successfully - {}",classTestResultsDtoList);
        return classTestResultsDtoList;
    }

    //get all test,exam and final exam results for one student in an academic year
    public List<ResultsDto> getStudentResult(String rollNumber,String className, String acYear) {
        Student student = studentService.getStudentFromClassRollNo(className,rollNumber);
        if(student==null){
            log.info("Student not found. Throws NotFoundException");
            throw new NotFoundException("Student not found");
        }
        List<Results> results = resultsRepository.findAllBystudentId(student.getStudentId());
        List<Results> resultsList = new ArrayList<>();
        if(results.isEmpty()){
            log.info("No results found for student - {}. Throws NotFoundException",student.getStudentId());
            throw new NotFoundException("No results found for student - "+ student.getName());
        }
        for (Results r :results) {
            Schedule schedule = scheduleService.getScheduleDetails(r.getScheduleCode());
            if(schedule==null){
                log.info("No schedule found. Throws NotFoundException");
                throw new NotFoundException("Schedule not found");
            }
            if(schedule.getYear().equals(acYear)){
                resultsList.add(r);
            }
        }
        List<ResultsDto> resultsDtoList = new ArrayList<>();
        for (Results r:
             resultsList) {
            Schedule s = scheduleService.getScheduleDetails(r.getScheduleCode());
            List<Double> aggregates= getAggregates(s.getScheduleType(),r.getMarksList());
            ResultsDto result = new ResultsDto(
                    r.getResultsCode(),
                    r.getStudentId(),
                    r.getScheduleCode(),
                    r.getMarksList(),
                    aggregates.get(0),
                    aggregates.get(1)
            );
           resultsDtoList.add(result);
        }
        log.info("Results found as - {}",resultsDtoList);
        return resultsDtoList;
    }

    //get the result percentage for a student in an academic year
    public double getResultPercentage(String rollNumber, String className, String acYear) {

        Student student = studentService.getStudentFromClassRollNo(className,rollNumber);
        if(student==null){
            log.info("Student not found. Throws NotFoundException");
            throw new NotFoundException("Action failed! Student not found");
        }
        ClassDto classDto = classService.getClassLevelById(student.getClassCode());
        if(classDto==null){
            log.info("Class not found. Throws NotFoundException");
            throw new NotFoundException("Action failed! CLass not found");
        }
        List<ResultsDto> resultsList = getStudentResult(rollNumber,className,acYear);
        if(resultsList.isEmpty()){
            log.info("No results found for student - {}. Throws NotFoundException",student.getName());
            throw new NotFoundException("No results found for student - "+student.getName());
        }
        //sum of internal marks of subjects in the FE + sum of external marks for FE (for one student)
        double totalObtainedMarks = 0;

        //sum of max internal and external marks for subjects
        double totalPossibleMarks = 0;

        List<ResultsDto> finalExamResults = new ArrayList<>();
        for (ResultsDto r : resultsList) {
            Schedule schedule = scheduleService.getScheduleDetails(r.getScheduleCode());
            if(schedule.getScheduleCode().startsWith("FE")){
                finalExamResults.add(r);
                break;
            }
        }
        if(finalExamResults.isEmpty()){
            log.info("Final exam results not found for student - {}",student);
            throw new NotFoundException("No results found");
        }
        ResultsDto results = finalExamResults.get(0);
        List<Marks> marksList = results.getMarksList();
        for (Marks m :marksList) {
            Subject sub = subjectService.getSubjectById(m.getSubjectCode()).orElse(null);
            if(sub==null){
                log.info("Subject not found. Throws NotFoundException");
                throw new NotFoundException("Subject not found");
            }
            totalObtainedMarks+=m.getInternalMarks()+m.getExternalMarks();
            totalPossibleMarks+=sub.getMaxTestMarks()+sub.getMaxExamMarks();
        }
        Double percentage;
        if(totalPossibleMarks!=0){
            percentage = (totalObtainedMarks * 100)/(totalPossibleMarks);
        }
        else{
            log.info("Division by zero is causing the error");
            throw new InvalidValueException("Action failed! Total possible marks for subjects cannot be zero");
        }
        log.info("Percentage found as - {}",percentage);
        return percentage;
    }

    //get a particular test result for a student
    public ResultsDto getStudentTestResult(String className, String acYear, String scName, String rollNo) {
        Student student = studentService.getStudentFromClassRollNo(className, rollNo);
        ResultsDto resultsDto = null;
        if(student==null){
            log.info("Student not found. Throws NotFoundException");
            throw new NotFoundException("Student Not Found!");
        }
        List<Results> results = resultsRepository.findAllBystudentId(student.getStudentId());
        if(results.isEmpty()){
            log.info("No results found for student - {}. Throws NotFoundException",student.getStudentId());
            throw new NotFoundException("No results found for student - "+ student.getName());
        }
        for (Results r :results) {
            Schedule schedule = scheduleService.getScheduleDetails(r.getScheduleCode());
            if (schedule == null) {
                log.info("Schedule not found. Throws NotFoundException");
                throw new NotFoundException("Schedule not found");
            }
            log.info("Schedule found as  - {}",schedule);
            if (schedule.getYear().equals(acYear) && schedule.getScheduleName().equals(scName)) {
                List<Double> aggregates= getAggregates(schedule.getScheduleType(),r.getMarksList());
                resultsDto = new ResultsDto(
                       r.getResultsCode(),
                       r.getStudentId(),
                       r.getScheduleCode(),
                        r.getMarksList(),
                        aggregates.get(0),
                        aggregates.get(1));
            }
        }
        if(resultsDto==null){
            throw new NotFoundException("Result not Found");
        }
        log.info("Result found as  - {}",resultsDto);
        return resultsDto;
    }

    //get top 5 students from a class in an academic year
    public List<StudentDto> getLeaderboard(String className, String acYear) {
            List<Student> studentList = studentService.getStudentDetailsByClassCode(classService.getClassCodeFromName(className));
            if(studentList.isEmpty()){
                throw new NotFoundException("No students found in studentList");
            }
            log.info("Student list found as - {}",studentList);
            List<StudentDto> studentMarkList = new ArrayList<>();
        for (Student s: studentList) {
            StudentDto student = new StudentDto(
                    s.getStudentId(),
                    s.getClassCode(),
                    s.getRollNumber(),
                    s.getName(),
                    getResultPercentage(s.getRollNumber(),className,acYear)
            );
            studentMarkList.add(student);
        }
        Collections.sort(studentMarkList,Collections.reverseOrder());
        log.info("Top 5 students found as - {}",studentMarkList.subList(0,5));
        if(studentMarkList.size()>5)
            return studentMarkList.subList(0,5);
        else return studentMarkList;
    }

    public List<StudentDto> getToppersList(String acYear) {
        List<StudentDto> toppersList = new ArrayList<>();
        List<ClassDto> classList = classService.getAllClasses();
        if(classList.isEmpty()){
            throw new NotFoundException("Class list is Empty");
        }
        //need to sort class by classLevel
        //code here
        for (ClassDto c :
             classList) {
            List<StudentDto> rankList = getLeaderboard(c.getName(),acYear);
            toppersList.add(rankList.get(0));
        }
        if(toppersList.isEmpty()){
            throw new NotFoundException("Toppers List is Empty");
        }
        return toppersList;
    }


    public List<Double> getAggregates(String scheduleType, List<Marks> marksList){
        Double totalMarks= (double) 0;
        Double maxTotal = (double) 0;
        Double percentage;
        if(scheduleType.equalsIgnoreCase("Test")){
            for (Marks m: marksList) {
                Subject sub = subjectService.getSubjectById(m.getSubjectCode()).orElse(null);
                totalMarks += m.getInternalMarks();
                maxTotal  += (double)sub.getMaxTestMarks();
            }
        }
        else if(scheduleType.equalsIgnoreCase("Exam")){
            for (Marks m: marksList) {
                Subject sub = subjectService.getSubjectById(m.getSubjectCode()).orElse(null);
                totalMarks += m.getExternalMarks();
                maxTotal  += (double)sub.getMaxExamMarks();
            }
        }
        else{
            for (Marks m: marksList ) {
                Subject sub = subjectService.getSubjectById(m.getSubjectCode()).orElse(null);
                totalMarks += m.getInternalMarks()+m.getExternalMarks();
                maxTotal  += (double) (sub.getMaxTestMarks()+sub.getMaxExamMarks());
            }
        }
        percentage = (totalMarks*100)/(maxTotal);
        List<Double> list = new ArrayList<Double>();
        list.add(totalMarks);
        list.add(percentage);
        return list;
    }
}