package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.Dto.StudentDto;
import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.model.*;
import com.target.targetreadyresultsservice.repository.ResultsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class ResultsServiceTest {

    private ResultsRepository resultsRepository;
    private ResultsService resultsService;
    private SubjectService subjectService;
    private ScheduleService scheduleService;
    private ClassService classService;
    private StudentService studentService;
    private Results results;

    @BeforeEach
    void setUp(){
        resultsRepository = mock(ResultsRepository.class);
        scheduleService = mock(ScheduleService.class);
        classService = mock(ClassService.class);
        studentService = mock(StudentService.class);
        subjectService = mock(SubjectService.class);
        resultsService = new ResultsService(resultsRepository,studentService,
                scheduleService, classService,subjectService);
    }

    @Test
    void addNewResultTestSuccess() {

        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,90)));

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Student student = new Student("4","Bob","C4","10");

        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.of(student));

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S999","Class ten subject",
                10,"C4",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        Results expected = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));
        expected.setResultsCode("R4TC420JULY2023");

        when(resultsRepository.save(any(Results.class))).thenReturn(result);

        Results actual = resultsService.addNewResult(result);

        assertEquals(expected.toString(),actual.toString());
    }

    @Test
    void addNewResultExamSuccess() {

        Results result = new Results("4","EC420JULY2023",
                List.of(new Marks("S999",45,90)));

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("EC420JULY2023","C4",subjectScheduleList,
                "Exam","model exam","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Student student = new Student("4","Bob","C4","10");

        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.of(student));

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S999","Class ten subject",
                10,"C4",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        Results expected = new Results("4","EC420JULY2023",
                List.of(new Marks("S999",0.0,90.0)));
        expected.setResultsCode("R4EC420JULY2023");

        when(resultsRepository.save(any(Results.class))).thenReturn(result);

        Results actual = resultsService.addNewResult(result);

        assertEquals(expected.toString(),actual.toString());
    }

    @Test
    void addNewResultInvalidScheduleType() {
        Results result = new Results("4","EC420JULY2023",
                List.of(new Marks("S999",45,90)));

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("EC420JULY2023","C4",subjectScheduleList,
                "normal","model exam","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Student student = new Student("4","Bob","C4","10");

        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.of(student));

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S999","Class ten subject",
                10,"C4",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        assertThrows(InvalidValueException.class,()->resultsService.addNewResult(result));
    }

    @Test
    void addNewResultMarksForAllSubjectNotIncluded() {
        Results result = new Results("4","EC420JULY2023",
                List.of(new Marks("S999",45,90)));

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true),
                new SubjectSchedule("S200", LocalDate.of(2023,7,24),
                        LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("EC420JULY2023","C4",subjectScheduleList,
                "exam","model exam","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Student student = new Student("4","Bob","C4","10");

        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.of(student));

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S999","Class ten subject",
                10,"C4",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        assertThrows(BlankValueException.class,()->resultsService.addNewResult(result));
    }

    @Test
    void addNewResultReturnsInvalidValueException(){

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",78,0)));

        Student student = new Student("4","Bob","C4","10");
        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.ofNullable(student));

        Subject subject = new Subject("S999","Class ten subject",
                10,"C10",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        assertThrows(InvalidValueException.class,()->resultsService.addNewResult(result));
    }

    @Test
    void updateResultTestSuccessful() {

        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,89)));

        Optional<Results> expected = Optional.of(new Results( "4", "TC420JULY2023",
                List.of(new Marks("S999", 50, 0))));

        when(resultsRepository.findById(any(String.class))).thenReturn(Optional.of(result));

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Student student = new Student("4","Bob","C4","10");

        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.of(student));

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S999","Class ten subject",
                10,"C10",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        Results update = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",50,0)));

        when(resultsRepository.save(any(Results.class))).thenReturn(update);

        Optional<Results> actual = resultsService.updateResult("R4TC420JULY2023",update);

        assertEquals(expected.toString(),actual.toString());
    }

    @Test
    void updateResultExamSuccessful() {
        Results result = new Results("4","EC420JULY2023",
                List.of(new Marks("S999",45,78)));

        Optional<Results> expected = Optional.of(new Results( "4", "EC420JULY2023",
                List.of(new Marks("S999", 0, 80))));

        when(resultsRepository.findById(any(String.class))).thenReturn(Optional.of(result));

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("EC420JULY2023","C4",subjectScheduleList,
                "exam","model exam","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Student student = new Student("4","Bob","C4","10");

        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.of(student));

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S999","Class four subject",
                10,"C4",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        Results update = new Results("4","EC420JULY2023",
                List.of(new Marks("S999",0,80)));

        when(resultsRepository.save(any(Results.class))).thenReturn(update);

        Optional<Results> actual = resultsService.updateResult("R4EC420JULY2023",update);

        assertEquals(expected.toString(),actual.toString());
    }

    @Test
    void updateResultExamInvalidMarks() {
        Results result = new Results("4","EC420JULY2023",
                List.of(new Marks("S999",45,178)));
        when(resultsRepository.findById(any(String.class))).thenReturn(Optional.of(result));

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("EC420JULY2023","C4",subjectScheduleList,
                "exam","model exam","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Student student = new Student("4","Bob","C4","10");

        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.of(student));

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S999","Class four subject",
                10,"C4",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        Results update = new Results("4","EC420JULY2023",
                List.of(new Marks("S999",0,80)));

        assertThrows(InvalidValueException.class,()->resultsService.updateResult("R4EC420JULY2023",update));
    }

    @Test
    void updateResultWithEmptyMarkList() {
        Results result = new Results("4","EC420JULY2023",
                List.of(new Marks("S999",45,0)));
        when(resultsRepository.findById(any(String.class))).thenReturn(Optional.of(result));

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("EC420JULY2023","C4",subjectScheduleList,
                "exam","model exam","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Student student = new Student("4","Bob","C4","10");

        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.of(student));

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S999","Class four subject",
                10,"C4",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        Results update = new Results("4","EC420JULY2023",
                Collections.EMPTY_LIST);

        assertThrows(BlankValueException.class,()->resultsService.updateResult("R4EC420JULY2023",update));
    }

    @Test
    void updateResultReturnsNotFoundException(){
        assertThrows(NotFoundException.class,()->resultsService.updateResult("R4JULY2023",results));
    }

    @Test
    void updateResultInvalidScheduleType() {
        Results result = new Results("4","EC420JULY2023",
                List.of(new Marks("S999",45,0)));
        when(resultsRepository.findById(any(String.class))).thenReturn(Optional.of(result));

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("EC420JULY2023","C4",subjectScheduleList,
                "normal","model exam","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Student student = new Student("4","Bob","C4","10");

        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.of(student));

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S999","Class four subject",
                10,"C4",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        Results update = new Results("4","EC420JULY2023",
                List.of(new Marks("S999",45,0)));

        assertThrows(InvalidValueException.class,()->resultsService.updateResult("R4EC420JULY2023",update));
    }

    @Test
    void getStudentResult(){
        Student student = new Student("2","Bob","C4","10");

        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        List<Results> resultsList = List.of(new Results("2","TC420JULY2023",
                List.of(new Marks("S999", 50, 0))));
        when(resultsRepository.findAllBystudentId(any(String.class))).thenReturn(resultsList);

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        List<Results> expected = List.of(new Results("2","TC420JULY2023",
                List.of(new Marks("S999", 50, 0))));

        List<Results> actual = resultsService.getStudentResult("2","4","2023-2024");

        assertEquals(expected.toString(),actual.toString());
    }

    @Test
    void getStudentResultReturnsNotFoundException(){
        Student student = new Student("2","Bob","C4","10");

        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        when(resultsRepository.findAllBystudentId(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class,()->resultsService.getStudentResult("2","4","2023-2024"));
    }

    @Test
    void getStudentResultNoStudentFound(){
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(null);
        assertThrows(NotFoundException.class,()->resultsService.getStudentResult("10","4","2023-2024"));
    }

    @Test
    void getStudentResultNoScheduleFound(){
        Student student = new Student("2","Bob","C4","10");

        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        List<Results> resultsList = List.of(new Results("2","TC420JULY2023",
                List.of(new Marks("S999", 50, 0))));
        when(resultsRepository.findAllBystudentId(any(String.class))).thenReturn(resultsList);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(null);
        assertThrows(NotFoundException.class,()->resultsService.getStudentResult("10","4","2023-2024"));
    }

    @Test
    void getResultPercentageSuccessful() {
        Student student = new Student("2","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        List<Results> resultsList = List.of(new Results("2","FEC420JULY2023",
                List.of(new Marks("S999", 45, 67))));
        when(resultsRepository.findAllBystudentId(any(String.class))).thenReturn(resultsList);

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("FEC420JULY2023","C4",subjectScheduleList,
                "final exam","Final exam","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Subject subject = new Subject("S999","Physics",10,"C4",100,50);
        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        Double expected = ((45.0+67)/(50+100))*100;

        Double actual = resultsService.getResultPercentage("10","4","2023-2024");

        assertEquals(expected,actual);
    }

    @Test
    void getResultPercentageReturnsNoStudentFound(){
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(null);
        assertThrows(NotFoundException.class,()->resultsService.getResultPercentage("10","4","2023-2024"));
    }

    @Test
    void getResultPercentageNoClassFound() {
        Student student = new Student("2","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        when(classService.getClassLevelById(any(String.class))).thenReturn(null);
        assertThrows(NotFoundException.class,()->resultsService.getResultPercentage("10","4",
                "2023-2024"));
    }

    @Test
    void getResultPercentageWithZeroMaxMark() {
        Student student = new Student("2","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        List<Results> resultsList = List.of(new Results("2","FEC420JULY2023",
                List.of(new Marks("S999", 45, 67))));
        when(resultsRepository.findAllBystudentId(any(String.class))).thenReturn(resultsList);

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("FEC420JULY2023","C4",subjectScheduleList,
                "final exam","Final exam","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Subject subject = new Subject("S999","Physics",10,"C4",0,0);
        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        assertThrows(InvalidValueException.class,()->resultsService.getResultPercentage("10","4",
                "2023-2024"));
    }

    @Test
    void getClassResultSuccessful(){
        when(classService.getClassCodeFromName(any(String.class))).thenReturn("C4");

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0),true));

        List<Schedule> schedule = List.of(new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",true),
                new Schedule("EC420JULY2022","C4",subjectScheduleList,
                        "exam","model exam","2022-2024",false));

        when(scheduleService.getScheduleByClass(any(String.class),any(String.class))).thenReturn(schedule);

        List<Results> resultsList = List.of(new Results("2","TC420JULY2023",
                List.of(new Marks("S999", 45, 0))));
        when(resultsRepository.findAllByscheduleCode(any(String.class))).thenReturn(resultsList);

        List<Results> expected = List.of(new Results("2","TC420JULY2023",
                List.of(new Marks("S999", 45, 0))));

        List<Results> actual = resultsService.getClassResult("4","2023-2024");

        assertEquals(expected.toString(),actual.toString());
    }

    @Test
    void getClassResultReturnsBlankValueException(){
        assertThrows(BlankValueException.class,()->resultsService.getClassResult("",""));
    }

    @Test
    void getClassResultReturnsAcYearBlank(){
        assertThrows(BlankValueException.class,()->resultsService.getClassResult("C4",""));
    }

    @Test
    void getClassResultWithNoSchedules() {
        when(classService.getClassCodeFromName(any(String.class))).thenReturn("C4");
        when(scheduleService.getScheduleByClass(any(String.class),any(String.class))).thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class,()->resultsService.getClassResult("C4","2023-2024"));
    }

    @Test
    void getClassResultReturnsNotFoundException() {
        when(classService.getClassCodeFromName(any(String.class))).thenReturn("");
        assertThrows(NotFoundException.class,()->resultsService.getClassResult("4","2023-2024"));
    }

    @Test
    void getClassResultWithNoResults(){
        when(classService.getClassCodeFromName(any(String.class))).thenReturn("C4");

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        List<Schedule> schedule = List.of(new Schedule("TC420JULY2023","C4",subjectScheduleList,
                        "Test","Class Test 1","2023-2024",true),
                new Schedule("EC420JULY2022","C4",subjectScheduleList,
                        "Test","Class Test 1","2022-2024",false));

        when(scheduleService.getScheduleByClass(any(String.class),any(String.class))).thenReturn(schedule);

        when(resultsRepository.findAllByscheduleCode(any(String.class))).thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class,()->resultsService.getClassResult("4","2023-2924"));
    }

    @Test
    void deleteResultSuccessful() {
        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));
        result.setResultsCode("R4JULY2023");
        when(resultsRepository.findById(any(String.class))).thenReturn(Optional.of(result));

        resultsService.deleteResult("R4JULY2023");
        verify(resultsRepository,times(1)).delete(result);
    }

    @Test
    void deleteResultReturnsNotFoundException(){
        when(resultsRepository.findById(any(String.class))).thenReturn(null);
        assertThrows(NotFoundException.class,()->resultsService.deleteResult(any(String.class)));
    }

    @Test
    void getAverageForSubjectSuccessful(){
        Student student = new Student("2","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        when(classService.getClassCodeFromName(any(String.class))).thenReturn("C4");

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule s1 = new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",false);
        Schedule s2 = new Schedule("TC420JUNE2023","C4",subjectScheduleList,
                "Test","Class Test 2","2023-2024",false);

        List<Schedule> schedule = List.of(s1,s2);

        when(scheduleService.getScheduleByClass(any(String.class),any(String.class))).thenReturn(schedule);

        List<Results> resultsList = List.of(new Results("2","TC420JULY2023",
                List.of(new Marks("S999", 45, 0))),
                new Results("2","TC420JUNE2023",
                        List.of(new Marks("S999", 30, 0))));
        when(resultsRepository.findAllByscheduleCode(any(String.class))).thenReturn(resultsList);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(s1);
        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(s2);

        Double expected = (45.0+30.0)/2;

        Double actual = resultsService.getAverageForSubject("10","4",
                "2023-2024","S999");

        assertEquals(expected,actual);
    }

    @Test
    void getAverageForSubjectReturnsNotFoundException() {
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(null);
        assertThrows(NotFoundException.class,()->resultsService.getAverageForSubject("10","4",
                "2023-2024","S999"));
    }

    @Test
    void getAverageForSubjectClassNotFound(){
        Student student = new Student("2","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        when(classService.getClassLevelById(any(String.class))).thenReturn(null);
        assertThrows(NotFoundException.class,()->resultsService.getAverageForSubject("2","4",
                "2023-2024","S999"));
    }

    @Test
    void getAverageForSubjectNoTestResultsFound() {
        Student student = new Student("2","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        when(classService.getClassCodeFromName(any(String.class))).thenReturn("C4");

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule s1 = new Schedule("EC420JULY2023","C4",subjectScheduleList,
                "exam","model exam","2023-2024",false);
        Schedule s2 = new Schedule("EC420JUNE2023","C4",subjectScheduleList,
                "exam","pre-model exam","2023-2024",false);

        List<Schedule> schedule = List.of(s1,s2);

        when(scheduleService.getScheduleByClass(any(String.class),any(String.class))).thenReturn(schedule);

        List<Results> resultsList = List.of(new Results("2","EC420JULY2023",
                        List.of(new Marks("S999", 45, 67))),
                new Results("2","EC420JUNE2023",
                        List.of(new Marks("S999", 30, 45))));
        when(resultsRepository.findAllByscheduleCode(any(String.class))).thenReturn(resultsList);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(s1);
        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(s2);

        assertThrows(InvalidValueException.class,()->resultsService.getAverageForSubject("10","4",
                "2023-2024","S999"));
    }

    @Test
    void getClassTestResultsSuccessful() {
        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassCodeFromName(any(String.class))).thenReturn(classDto.getName());

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule s1 = new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",false);
        Schedule s2 = new Schedule("TC420JUNE2023","C4",subjectScheduleList,
                "Test","Class Test 2","2023-2024",false);

        List<Schedule> scheduleList = List.of(s1,s2);
        when(scheduleService.getScheduleByClass(any(String.class),any(String.class))).thenReturn(scheduleList);

        Results r1 = new Results("2","TC420JULY2023",
                List.of(new Marks("S999", 45, 0)));
        Results r2 = new Results("4","TC420JULY2023",
                List.of(new Marks("S999", 30, 0)));
        List<Results> resultsList = List.of(r1,r2);
        when(resultsRepository.findAllByscheduleCode(any(String.class))).thenReturn(resultsList);

        List<Results> expected = List.of(r1,r2);
        List<Results> actual = resultsService.getClassTestResults("4","2023-2024","Class Test 1");

        assertEquals(expected,actual);
    }

    @Test
    void getClassTestResultsReturnsBlankValueException() {
        assertThrows(BlankValueException.class,()->resultsService.getClassTestResults("","",","));
    }

    @Test
    void getClassTestResultsAcYearBlank(){
        assertThrows(BlankValueException.class,()->resultsService.getClassTestResults("4","",""));
    }

    @Test
    void getClassTestResultsScNameBlank(){
        assertThrows(BlankValueException.class,()->resultsService.getClassTestResults("4",
                "2023-2024",""));
    }

    @Test
    void getClassTestResultsClassNotFound() {
        when(classService.getClassCodeFromName(any(String.class))).thenReturn("");
        assertThrows(InvalidValueException.class,()->resultsService.getClassTestResults("4",
                "2023-2024","Test 1"));
    }

    @Test
    void getClassTestResultsSchedulesNotFound() {
        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassCodeFromName(any(String.class))).thenReturn(classDto.getName());

        when(scheduleService.getScheduleByClass(any(String.class),any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class,()->resultsService.getClassTestResults("4",
                "2023-2024","Test 1"));
    }

    @Test
    void getClassTestResultsNotFound(){
        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getClassCodeFromName(any(String.class))).thenReturn(classDto.getName());

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule s1 = new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",false);
        Schedule s2 = new Schedule("TC420JUNE2023","C4",subjectScheduleList,
                "Test","Class Test 2","2023-2024",false);

        List<Schedule> scheduleList = List.of(s1,s2);
        when(scheduleService.getScheduleByClass(any(String.class),any(String.class))).thenReturn(scheduleList);

        when(resultsRepository.findAllByscheduleCode(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class,()->resultsService.getClassTestResults("4",
                "2023-2024","Test 1"));
    }

    @Test
    void getStudentTestResultSuccessful() {
        Student student = new Student("2","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        Results r1 = new Results("2","TC420JULY2023",
                List.of(new Marks("S999", 45, 0)));

        List<Results> resultsList = List.of(r1);
        when(resultsRepository.findAllBystudentId(any(String.class))).thenReturn(resultsList);

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), false));
        Schedule s1 = new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",false);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(s1);

        Results expected = new Results("2","TC420JULY2023",
                List.of(new Marks("S999", 45, 0)));

        Results actual = resultsService.getStudentTestResult("4","2023-2024","Class Test 1","10");

        assertEquals(expected.toString(),actual.toString());
    }

    @Test
    void getStudentTestResultStudentNotFound() {
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(null);
        assertThrows(NotFoundException.class,()->resultsService.getStudentTestResult("4",
                "2023-2024","Class Test 1","10"));
    }

    @Test
    void getStudentTestResultNotFound() {
        Student student = new Student("2","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);
        when(resultsRepository.findAllBystudentId(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class,()->resultsService.getStudentTestResult("4","2023-2024",
                "test 1","10"));
    }

    @Test
    void getStudentTestResultScheduleNotFound() {
        Student student = new Student("2","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        Results r1 = new Results("2","TC420JULY2023",
                List.of(new Marks("S999", 45, 0)));

        List<Results> resultsList = List.of(r1);
        when(resultsRepository.findAllBystudentId(any(String.class))).thenReturn(resultsList);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(null);
        assertThrows(NotFoundException.class,()->resultsService.getStudentTestResult("4","2023-2024",
                "test 1","10"));
    }

    @Test
    void getLeaderboardTestingForSuccess() {
        List<Student> studentList = List.of(
                new Student("1","Student1","C4","10"),
                new Student("2","Student2","C4","11"),
                new Student("3","Student3","C4","12"),
                new Student("4","Student4","C4","13"),
                new Student("5","Student5","C4","14")
        );

        List<Results> resultsList = List.of(
                new Results("1","FE420JULY2023",
                        List.of(new Marks("S999", 50, 100))),
                new Results("2","FE420JULY2023",
                        List.of(new Marks("S999", 40, 76))),
                new Results("3","FE420JULY2023",
                        List.of(new Marks("S999", 39, 76))),
                new Results("4","FE420JULY2023",
                        List.of(new Marks("S999", 33, 80))),
                new Results("5","FE420JULY2023",
                        List.of(new Marks("S999", 29, 50)))
        );

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), false));
        Schedule s1 = new Schedule("FEC420JULY2023","C4",subjectScheduleList,
                "final exam","final exam","2023-2024",false);
        Subject subject = new Subject("S999","Class four subject",
                10,"C4",100,50);

        when(classService.getClassCodeFromName(any(String.class))).thenReturn("C4");
        when(studentService.getStudentDetailsByClassCode(any(String.class))).thenReturn(studentList);

        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        int count = 0;
        for (Student s : studentList) {
            when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(s);
            when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

            //goes to getStudentResult function
            when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(s);
            List<Results> resultListForOne = List.of(resultsList.get(count));
            when(resultsRepository.findAllBystudentId(any(String.class))).thenReturn(resultListForOne);

           // count++;
            for (Results r : resultListForOne) {
                when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(s1);
            }

            //back to percentage
            when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(s1);

            List<Marks> marksList = resultsList.get(count).getMarksList();
            for (Marks m : marksList) {
                when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));
            }
            count++;
        }
        List<StudentDto> studentDtoList = resultsService.getLeaderboard("4","2023-2024");
        assertEquals(5,studentDtoList.size());
        assertEquals(studentList.get(0).getName(),studentDtoList.get(0).getName());
        assertEquals(studentList.get(1).getName(),studentDtoList.get(1).getName());
        assertEquals(studentList.get(2).getName(),studentDtoList.get(2).getName());
        assertEquals(studentList.get(3).getName(),studentDtoList.get(3).getName());
        assertEquals(studentList.get(4).getName(),studentDtoList.get(4).getName());
    }

    @Test
    void getLeaderboardReturnsException() {
        when(studentService.getStudentDetailsByClassCode(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class,()->resultsService.getLeaderboard("4","2023-2024"));
    }
}