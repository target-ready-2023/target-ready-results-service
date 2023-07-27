package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.Exception.NullValueException;
import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.model.SubjectSchedule;
import com.target.targetreadyresultsservice.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ScheduleServiceTest {

    private ScheduleRepository scheduleRepository;
    private ScheduleService scheduleService;
    private ClassService classService;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        scheduleRepository = mock(ScheduleRepository.class);
        classService = mock(ClassService.class);
        scheduleService = new ScheduleService(scheduleRepository, classService);
    }

    @Test
    void addNewScheduleTest() {

        Schedule schedule = new Schedule();

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,10),
                LocalTime.of(10,00), 50, true));

        schedule.setScheduleName("Class Test 1");
        schedule.setScheduleType("Test");
        schedule.setClassCode("C99");
        schedule.setScheduleStatus(true);
        schedule.setSubjectSchedule(subjectScheduleList);

        String expected = new Schedule("TC9910JULY2023","C99",
                subjectScheduleList,"Test",
                "Class Test 1",
                true).toString();


        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        String actual = scheduleService.addNewSchedule(schedule).toString();

        assertEquals(expected,actual);
    }

    @Test
    void addNewScheduleReturnsBlankValueException(){
        Schedule schedule = new Schedule();

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,10),
                LocalTime.of(10,00), 50, true));

        schedule.setScheduleName("");
        schedule.setScheduleType("Test");
        schedule.setClassCode("C99");
        schedule.setScheduleStatus(true);
        schedule.setSubjectSchedule(subjectScheduleList);

        assertThrows(BlankValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleReturnsInvalidValueException(){
        Schedule schedule = new Schedule();

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,16),
                LocalTime.of(10,00), 50, true));

        schedule.setScheduleName("Class Test 1");
        schedule.setScheduleType("Test");
        schedule.setClassCode("C99");
        schedule.setScheduleStatus(true);
        schedule.setSubjectSchedule(subjectScheduleList);

        assertThrows(InvalidValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void getAllSchedulesTest(){
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,16),
                LocalTime.of(10,00),
                50, true));

       List<Schedule> scheduleList = List.of(new Schedule("C99",subjectScheduleList,
               "Test","Class Test 1",true));
       when(scheduleRepository.findAll()).thenReturn(scheduleList);
       List<Schedule> schedules = scheduleService.findAll();
       assertThat(schedules).isNotNull();
       assertThat(1).isEqualTo(schedules.size());
    }

    @Test
    void getAllSchedulesReturnsEmpty(){
        when(scheduleRepository.findAll()).thenReturn(new ArrayList<>());
        List<Schedule> schedules = scheduleService.findAll();
        assertEquals(Collections.EMPTY_LIST,schedules);
    }

    @Test
    void getScheduleByIdTest(){
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00),
                50, true));
        Schedule schedule = new Schedule("TC9910JULY2023","C99",subjectScheduleList,
                "Test","Class Test 1",true);

        when(scheduleRepository.findById(any(String.class))).thenReturn(Optional.of(schedule));

        Schedule actual = scheduleService.getScheduleDetails("TC9910JULY2023");
        assertThat(actual).isNotNull();
        assertTrue(schedule.getScheduleCode().equals(actual.getScheduleCode()));
    }

    @Test
    void getScheduleByIDReturnsNotFoundException(){
        when(scheduleRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(schedule));
        assertThrows(NotFoundException.class,()->scheduleService.getScheduleDetails("TC9910JULY2023"));
    }

    @Test
    void getActiveScheduleByClassTest(){
        List<Schedule> schedules = new ArrayList<>();
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), 50, true));
        schedules.add(new Schedule("TC9910JULY2023","C99",subjectScheduleList,
                "Test","Class Test 1",true));

        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(schedules);

        List<Schedule> response = scheduleService.getactiveSchedule("C99");

        assertEquals(schedules,response);

    }

    @Test
    void getActiveScheduleByClassReturnsNullValueException(){
        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NullValueException.class,()->scheduleService.getactiveSchedule("C99"));
    }

    @Test
    void getSchedulesByClassTest(){
        List<Schedule> schedules = new ArrayList<>();
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00),
                50, false));
        schedules.add(new Schedule("TC9910JULY2023","C99",subjectScheduleList,
                "Test","Class Test 1",false));

        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(schedules);

        List<Schedule> response = scheduleService.getScheduleByClass("C99");

        assertEquals(schedules,response);
    }

    @Test
    void getScheduleByClassReturnsNullValueException(){
        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NullValueException.class,()->scheduleService.getactiveSchedule("C99"));
    }

    @Test
    void getScheduleForResults(){
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00),
                50, true));
        Schedule schedule = new Schedule("TC9910JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",true);

        List<Schedule> scheduleList = List.of(new Schedule("TC9910JULY2023","C4",subjectScheduleList,
                "Test","Class Test 1","2023-2024",true));

        List<ClassDto> classDtoList = List.of(new ClassDto("C4","4",List.of("Physics","social")));
        when(classService.getAllClasses()).thenReturn(classDtoList);
        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(scheduleList);

        Schedule actual = scheduleService.getScheduleForResult("Class Test 1","4","2023-2024");

        assertEquals(schedule.toString(),actual.toString());
    }

    @Test
    void getScheduleForResultsReturnsNotFoundException() {
        when(classService.getAllClasses()).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class,()->scheduleService.getScheduleForResult("Class Test 1",
                "4","2023-2024"));
    }

    @Test
    void getScheduleForResultsReturnsInvalidValueException() {

        List<ClassDto> classDtoList = List.of(new ClassDto("C4","4",List.of("Physics","social")));
        when(classService.getAllClasses()).thenReturn(classDtoList);

        assertThrows(InvalidValueException.class,()->scheduleService.getScheduleForResult("Class Test 1",
                "12","2023-2024"));
    }

    @Test
    void getScheduleForResultsReturnsBlankValueException(){
        assertThrows(BlankValueException.class,()->scheduleService.getScheduleForResult("","",""));
    }

    @Test
    void deleteScheduleByIdTest(){
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00),
                50, true));
        Schedule schedule = new Schedule("TC9910JULY2023","C99",subjectScheduleList,
                "Test","Class Test 1",true);
        when(scheduleRepository.findById(any(String.class))).thenReturn(Optional.of(schedule));
        scheduleService.deleteSchedule("TC9910JULY2023");
        verify(scheduleRepository,times(1)).delete(schedule);
    }

    @Test
    void deleteScheduleByIdReturnsNotFoundException(){
        assertThrows(NotFoundException.class,()->scheduleService.deleteSchedule("TC9910JULY2023"));
    }

    @Test
    void updateScheduleTest(){
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,25),
                LocalTime.of(10,00),
                50, true));
        schedule = new Schedule("TC9910JULY2023", "C99", subjectScheduleList,
                "Test", "Class Test 1", true);

        when(scheduleRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(schedule));

        Optional<Schedule> expected = Optional.of(new Schedule("TC9910JULY2023", "C99",
                subjectScheduleList, "Test",
                "Class Test 2",
                true));

        Schedule update = new Schedule("TC9910JULY2023", "C99",
                subjectScheduleList, "Test",
                "Class Test 2",
                true);

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(update);

        Optional<Schedule> actual = scheduleService.updateSchedule("TC9910JULY2023",update);

        assertEquals(expected.toString(),actual.toString());
    }

    @Test
    void updateScheduleTestReturnsNotFoundException(){
        assertThrows(NotFoundException.class,()->scheduleService.updateSchedule("TC9910JULY2023",schedule));
    }

}