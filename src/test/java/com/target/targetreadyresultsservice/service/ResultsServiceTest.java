package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.model.*;
import com.target.targetreadyresultsservice.repository.ResultsRepository;
import com.target.targetreadyresultsservice.repository.ScheduleRepository;
import com.target.targetreadyresultsservice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ResultsServiceTest {

    private ScheduleRepository scheduleRepository;
    private StudentRepository studentRepository;
    private ResultsRepository resultsRepository;
    private ResultsService resultsService;
    private ScheduleService scheduleService;
    private ClassService classService;

    @BeforeEach
    void setUp(){
        resultsRepository = mock(ResultsRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        studentRepository = mock(StudentRepository.class);
        scheduleService = mock(ScheduleService.class);
        classService = mock(ClassService.class);
        resultsService = new ResultsService(resultsRepository,studentRepository,scheduleRepository, scheduleService, classService);
    }

    @Test
    void addNewResult() {
        Student student = new Student("4","Bob","C4","10");

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00),
                50, true));
        Schedule schedule = new Schedule("TC420JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1",true);

        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,78)));

        Results expected = new Results("R4JULY2023","4","TC420JULY2023",
                List.of(new Marks("S999",45,78)));

        when(studentRepository.findById(any(String.class))).thenReturn(Optional.of(student));
        when(scheduleRepository.findById(any(String.class))).thenReturn(Optional.of(schedule));

        when(resultsRepository.save(any(Results.class))).thenReturn(result);

        Results actual = resultsService.addNewResult(result);

        assertEquals(expected.toString(),actual.toString());
    }

    @Test
    void addNewResultReturnsNotFoundException(){
        when(studentRepository.findById(any(String.class))).thenReturn(null);

    }

    @Test
    void updateResult() {

        Results result = new Results("R4JULY2023","4","TC420JULY2023",
                List.of(new Marks("S999",45,78)));

        Optional<Results> expected = Optional.of(new Results("R4JULY2023", "4", "TC420JULY2023",
                List.of(new Marks("S999", 50, 80))));

        when(resultsRepository.findById(any(String.class))).thenReturn(Optional.of(result));

        Results update = new Results("R4JULY2023","4","TC420JULY2023",
                List.of(new Marks("S999",50,80)));

        Optional<Results> actual = resultsService.updateResult("R4JULY2023",update);

        assertEquals(expected.toString(),actual.toString());
    }
}