package com.target.targetreadyresultsservice.service;

import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class StudentServiceTest {
    private StudentRepository studentRepository;
    private StudentService studentService;
    private ClassService classService;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        classService = mock(ClassService.class);
        studentService = new StudentService(studentRepository,classService);
    }

    @Test
    void getAllStudentsSuccessful() {
        List<Student> studentList = List.of(new Student("4","Bob","C4","10"));
        when(studentRepository.findAll()).thenReturn(studentList);

        List<Student> expected = studentList;
        List<Student> actual = studentService.getAllStudents();
        assertEquals(expected,actual);
    }

    @Test
    void getStudentInfoSuccessful() {
        Student student = new Student("4","Bob","C4","10");
        when(studentRepository.findById(any(String.class))).thenReturn(Optional.of(student));

        Optional<Student> expected = Optional.of(student);
        Optional<Student> actual = studentService.getStudentInfo("4");
        assertEquals(expected,actual);
    }

    @Test
    void getStudentDetailsByClassCodeSuccessful() {
        List<Student> studentList = List.of(new Student("4","Bob","C4","10"));
        when(studentRepository.findByclassCode(any(String.class))).thenReturn(studentList);

        List<Student> expected = studentList;
        List<Student> actual = studentService.getStudentDetailsByClassCode("C4");
        assertEquals(expected,actual);
    }

    @Test
    void getStudentByNameSuccessful() {
        List<Student> studentList = List.of(new Student("4","Bob","C4","10"));
        when(studentRepository.findByName(any(String.class))).thenReturn(studentList);

        List<Student> expected = studentList;
        List<Student> actual = studentService.getStudentByName("Bob");
        assertEquals(expected,actual);
    }

    @Test
    void getStudentFromClassRollNoSuccessful() {
        ClassDto classDto = new ClassDto("C4","4",List.of("S999"));
        when(classService.getAllClasses()).thenReturn(List.of(classDto));

        List<Student> studentList = List.of(new Student("4","Bob","C4","10"));
        when(studentRepository.findByclassCode(any(String.class))).thenReturn(studentList);

        Student expected = new Student("4","Bob","C4","10");
        Student actual = studentService.getStudentFromClassRollNo("4","10");

        assertEquals(expected.toString(),actual.toString());
    }
}