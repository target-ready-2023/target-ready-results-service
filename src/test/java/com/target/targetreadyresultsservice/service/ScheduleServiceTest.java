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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.linesOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ScheduleServiceTest {

    private ScheduleRepository scheduleRepository;
    private ScheduleService scheduleService;
    private ClassService classService;
    private SubjectService subjectService;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        scheduleRepository = mock(ScheduleRepository.class);
        classService = mock(ClassService.class);
        subjectService = mock(SubjectService.class);
        scheduleService = new ScheduleService(scheduleRepository, classService, subjectService);
    }

    private static final Logger log = LoggerFactory.getLogger(ScheduleServiceTest.class);

    @Test
    void addNewScheduleSuccess1() {

        Schedule schedule = new Schedule();

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,10),
                LocalTime.of(10,30),true));

        schedule.setScheduleName("Class Test 1");
        schedule.setScheduleType("Test");
        schedule.setClassCode("C99");
        schedule.setScheduleStatus(true);
        schedule.setSubjectSchedule(subjectScheduleList);

        String expected = new Schedule("TC9910JULY2023","C99",
                subjectScheduleList,"Test",
                "Class Test 1",
                true).toString();

        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));

        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S_PhyC99","Physics",10,"C99",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        String actual = scheduleService.addNewSchedule(schedule).toString();
        log.info("the actual value is  -{} ",actual);

        assertEquals(expected,actual);
    }

    @Test
    void addNewScheduleSuccess2() {

        Schedule schedule = new Schedule();

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,2,10),
                LocalTime.of(10,0),true));

        schedule.setScheduleName("model exam");
        schedule.setScheduleType("exam");
        schedule.setClassCode("C99");
        schedule.setScheduleStatus(true);
        schedule.setSubjectSchedule(subjectScheduleList);

        String expected = new Schedule("EC9910FEBRUARY2023","C99",
                subjectScheduleList,"exam",
                "model exam",
                true).toString();

        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));

        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S_PhyC99","Physics",10,"C99",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        String actual = scheduleService.addNewSchedule(schedule).toString();

        assertEquals(expected,actual);
    }

    @Test
    void addNewScheduleInvalidDate() {
        Schedule schedule = new Schedule();

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,4,10),
                LocalTime.of(10,0),true));

        schedule.setScheduleName("Class Test 1");
        schedule.setScheduleType("Test");
        schedule.setClassCode("C99");
        schedule.setScheduleStatus(true);
        schedule.setSubjectSchedule(subjectScheduleList);

        String expected = new Schedule("TC9910APRIL2023","C99",
                subjectScheduleList,"Test",
                "Class Test 1",
                true).toString();

        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));

        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S_PhyC99","Physics",10,"C99",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));
        assertThrows(InvalidValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleReturnsBlankValueException(){
        Schedule schedule = new Schedule();

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,10),
                LocalTime.of(10,0), true));

        schedule.setScheduleName(" ");
        schedule.setScheduleType("Test");
        schedule.setClassCode("C99");
        schedule.setScheduleStatus(true);
        schedule.setSubjectSchedule(subjectScheduleList);

        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));

        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        assertThrows(BlankValueException.class,()->scheduleService.addNewSchedule(schedule));
    }
    @Test
    void addNewScheduleWithNullClass() {
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), true));
        Schedule schedule = new Schedule(null,subjectScheduleList,
                "Test","Class Test 1",true);
        assertThrows(NullValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleWithBlankClass() {
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), true));
        Schedule schedule = new Schedule("",subjectScheduleList,
                "Test","Class Test 1",true);
        assertThrows(BlankValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleClassNotFound() {
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), true));
        Schedule schedule = new Schedule("C4",subjectScheduleList,
                "Test","Class Test 1",true);
        when(classService.getClassLevelById(any(String.class))).thenReturn(null);
        assertThrows(NotFoundException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleWithTypeBlank() {
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), true));
        Schedule schedule = new Schedule("C4",subjectScheduleList,
                "","Class Test 1",true);
        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);
        assertThrows(BlankValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleWithEmptySubjectList() {
        Schedule schedule = new Schedule("C4", Collections.EMPTY_LIST,
                "test", "Class Test 1", true);
        ClassDto classDto = new ClassDto("C99", "99", List.of("Physics"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);
        assertThrows(BlankValueException.class, () -> scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleWithBlankSubCode() {
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), true));
        Schedule schedule = new Schedule("C4",subjectScheduleList,
                "test","Class Test 1",true);
        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);
        assertThrows(BlankValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleSubNotTaught() {
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), true));
        Schedule schedule = new Schedule("C4",subjectScheduleList,
                "test","Class Test 1",true);
        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);
        Subject subject = new Subject("S999","Physics",10,"C99",100,50);
        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));
        assertThrows(InvalidValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleDateNull() {
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                null, LocalTime.of(10,00), true));
        Schedule schedule = new Schedule("C99",subjectScheduleList,
                "test","Class Test 1",true);
        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);
        Subject subject = new Subject("S999","Physics",10,"C99",100,50);
        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));
        assertThrows(BlankValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleTimeNull() {
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                null, true));
        Schedule schedule = new Schedule("C99",subjectScheduleList,
                "test","Class Test 1",true);
        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);
        Subject subject = new Subject("S999","Physics",10,"C99",100,50);
        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));
        assertThrows(BlankValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleInvalidTime() {
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(23,00), true));
        Schedule schedule = new Schedule("C99",subjectScheduleList,
                "test","Class Test 1",true);
        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));
        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);
        Subject subject = new Subject("S999","Physics",10,"C99",100,50);
        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));
        assertThrows(InvalidValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void addNewScheduleReturnsInvalidValueException(){
        Schedule schedule = new Schedule();

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,16),
                LocalTime.of(10,00), true));

        schedule.setScheduleName("Class Test 1");
        schedule.setScheduleType("Test");
        schedule.setClassCode("C99");
        schedule.setScheduleStatus(true);
        schedule.setSubjectSchedule(subjectScheduleList);

        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));

        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S_PhyC99","Physics",10,"C99",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

        assertThrows(InvalidValueException.class,()->scheduleService.addNewSchedule(schedule));
    }

    @Test
    void getAllSchedulesTest(){
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,16),
                LocalTime.of(10,00), true));

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
                LocalTime.of(10,00), true));
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
                LocalTime.of(10,00), true));
        schedules.add(new Schedule("TC9910JULY2023","C99",subjectScheduleList,
                "Test","Class Test 1","2023-2024",true));

        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(schedules);

        List<Schedule> response = scheduleService.getActiveSchedule("C99");

        assertEquals(schedules,response);

    }

    @Test
    void getActiveScheduleByClassReturnsNullValueException(){
        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NullValueException.class,()->scheduleService.getActiveSchedule("C99"));
    }

    @Test
    void getSchedulesByClassTest(){
        List<Schedule> schedules = new ArrayList<>();
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), false));
        schedules.add(new Schedule("TC9910JULY2023","C99",subjectScheduleList,
                "Test","Class Test 1","2023-2024",false));

        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(schedules);

        List<Schedule> response = scheduleService.getScheduleByClass("C99","2023-2024");

        assertEquals(schedules,response);
    }

    @Test
    void getScheduleByClassReturnsNullValueException(){
        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NullValueException.class,()->scheduleService.getActiveSchedule("C99"));
    }

    @Test
    void getScheduleForResults(){
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), true));
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
        assertThrows(BlankValueException.class,()->scheduleService.getScheduleForResult("","4","2023-2024"));
    }

    @Test
    void getScheduleForResultsWithBlankClass(){
        assertThrows(BlankValueException.class,()->scheduleService.getScheduleForResult("Test 1","","20203-2024"));
    }

    @Test
    void getScheduleForResultsWithBlankAcYear(){
        assertThrows(BlankValueException.class,()->scheduleService.getScheduleForResult("Test 1","4",""));
    }

    @Test
    void getScheduleForResultsWithEmptySchedule() {
        List<ClassDto> classDtoList = List.of(new ClassDto("C4","4",List.of("Physics","social")));
        when(classService.getAllClasses()).thenReturn(classDtoList);
        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class,()->scheduleService.getScheduleForResult("test 1","4","2023-2024"));
    }

    @Test
    void deleteScheduleByIdTest(){
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), true));
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
                LocalTime.of(10,00), true));
        schedule = new Schedule("TC9910JULY2023", "C99", subjectScheduleList,
                "Test", "Class Test 1", true);

        when(scheduleRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(schedule));

        ClassDto classDto = new ClassDto("C99","99",List.of("Physics"));

        when(classService.getClassLevelById(any(String.class))).thenReturn(classDto);

        Subject subject = new Subject("S_PhyC99","Physics",10,"C99",100,50);

        when(subjectService.getSubjectById(any(String.class))).thenReturn(Optional.of(subject));

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

    @Test
    void getScheduleNamesForClassSuccessful() {
        List<ClassDto> classDto = List.of(new ClassDto("C99","99",List.of("Physics")));
        when(classService.getClassLeveByName(any(String.class))).thenReturn(classDto);

        List<Schedule> schedules = new ArrayList<>();
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), false));
        schedules.add(new Schedule("TC9910JULY2023","C99",subjectScheduleList,
                "Test","Class Test 1","2023-2024",false));

        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(schedules);

        List<String> expected = List.of("Class Test 1");
        List<String> actual = scheduleService.getScheduleNamesForClass("99","2023-2024");
        assertEquals(expected,actual);
    }

    @Test
    void getScheduleNamesForClassListNotFound() {
        when(classService.getClassLeveByName(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class,()->scheduleService.getScheduleNamesForClass("4","2023-2024"));
    }

    @Test
    void getScheduleByYearSuccessful() {
        List<Schedule> schedules = new ArrayList<>();
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), false));
        schedules.add(new Schedule("TC9910JULY2023","C99",subjectScheduleList,
                "Test","Class Test 1","2023-2024",false));
        when(scheduleRepository.findByyear(any(String.class))).thenReturn(schedules);
        List<Schedule> actual = scheduleService.getScheduleByYear("2023-2024");
        assertEquals(schedules,actual);
    }

    @Test
    void getScheduleByYearEmpty() {
        when(scheduleRepository.findByyear(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class,()->scheduleService.getScheduleByYear("2023-2024"));
    }

    @Test
    void getScheduleByYearBlankAcYear() {
        assertThrows(BlankValueException.class,()->scheduleService.getScheduleByYear(""));
    }

    @Test
    void getScheduleByYearNullAcYear() {
        assertThrows(NullValueException.class,()->scheduleService.getScheduleByYear(null));
    }

    @Test
    void getScheduleAcYearsForClassSuccess() {
        List<Schedule> schedules = new ArrayList<>();
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023,7,20),
                LocalTime.of(10,00), false));
        schedules.add(new Schedule("TC9910JULY2023","C99",subjectScheduleList,
                "Test","Class Test 1","2023-2024",false));
        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(schedules);

        List<String> expected = List.of("2023-2024");
        List<String> actual = scheduleService.getScheduleAcYearsForClass("C99");
        assertEquals(expected,actual);
    }

    @Test
    void getScheduleAcYearsForClassEmpty() {
        when(scheduleRepository.findByclassCode(any(String.class))).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class, () -> scheduleService.getScheduleAcYearsForClass("C99"));
    }

    @Test
    void getScheduleAcYearsForClassBlank() {
        assertThrows(BlankValueException.class, () -> scheduleService.getScheduleAcYearsForClass(" "));
    }
}