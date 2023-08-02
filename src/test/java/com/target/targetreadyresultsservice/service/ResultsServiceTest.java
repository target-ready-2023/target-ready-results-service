package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Dto.ClassDto;
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
    void addNewResult() {

        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        Schedule schedule = new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",true);

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Student student = new Student("4","Bob","C4","10");

        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.of(student));

        Subject subject = new Subject("S999","Class ten subject",
                10,"C10",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        Results expected = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));
        expected.setResultsCode("R420JULY2023");

        when(resultsRepository.save(any(Results.class))).thenReturn(result);

        Results actual = resultsService.addNewResult(result);

        assertEquals(expected.toString(),actual.toString());
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
    void updateResult() {

        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));

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

        Subject subject = new Subject("S999","Class ten subject",
                10,"C10",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        Results update = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",50,0)));

        when(resultsRepository.save(any(Results.class))).thenReturn(update);

        Optional<Results> actual = resultsService.updateResult("R4JULY2023",update);

        assertEquals(expected.toString(),actual.toString());
    }

    @Test
    void updateResultReturnsNotFoundException(){
        assertThrows(NotFoundException.class,()->resultsService.updateResult("R4JULY2023",results));
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
    void getResultPercentage() {
        Student student = new Student("2","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        ClassDto classDto = new ClassDto("C4","4",List.of("Physics","Maths"));
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

        List<Results> results = resultsService.getStudentResult("2","4","2023-2024");

        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        Subject subject = new Subject("S999","Physics",10,"C4",100,50);
        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        Double expected = ((45.0+67)/(50+100))*100;

        Double actual = resultsService.getResultPercentage("10","4","2023-2024");

        assertEquals(expected,actual);
    }

    @Test
    void getResultPercentageReturnsNotFoundException(){
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(null);

        assertThrows(NotFoundException.class,()->resultsService.getResultPercentage("10","4","2023-2024"));
    }

    @Test
    void getClassResultSuccessful(){
        when(classService.getClassCodeFromName(any(String.class))).thenReturn("C4");

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10, 0), true));

        List<Schedule> schedule = List.of(new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",true),
                new Schedule("EC420JULY2022","C4",subjectScheduleList,
                        "Test","Class Test 1","2022-2024",false));

        when(scheduleService.getScheduleByClass(any(String.class))).thenReturn(schedule);

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
    void getClassResultReturnsNotFoundException() {
        when(classService.getClassCodeFromName(any(String.class))).thenReturn("");
        assertThrows(NotFoundException.class,()->resultsService.getClassResult("4","2023-2024"));
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
    void getAverageForSubjectSuccessful(){
        Student student = new Student("2","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        ClassDto classDto = new ClassDto("C4","4",List.of("Physics","Maths"));
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

        when(scheduleService.getScheduleByClass(any(String.class))).thenReturn(schedule);

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
    void getAverageForSubjectR
}