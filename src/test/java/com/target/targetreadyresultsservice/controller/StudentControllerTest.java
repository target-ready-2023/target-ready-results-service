package com.target.targetreadyresultsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = StudentController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class StudentControllerTest {
    private static final String END_POINT_PATH = "/student/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getStudentDetailsSuccessful() throws Exception{
        Student student = new Student("4","Bob","C4","10");
        when(studentService.getStudentInfo(any(String.class))).thenReturn(Optional.of(student));

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/student?studentId=4")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getStudentDetailsNotFound() throws Exception {
        when(studentService.getStudentInfo(any(String.class))).thenReturn(null);
        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/student?studentId=4")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void setStudentDetailsSuccessful() throws Exception{
        Student student = new Student("4","Bob","C4","10");
        studentService.setStudentInfo(any(Student.class));

        ResultActions response = mockMvc.perform(post(END_POINT_PATH+"/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getStudentsByClassCodeSuccessful() throws Exception{
        List<Student> studentList = List.of(new Student("4","Bob","C4","10"));
        when(studentService.getStudentDetailsByClassCode(any(String.class))).thenReturn(studentList);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/student/C4")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getStudentsByClassCodeException() throws Exception{
        when(studentService.getStudentDetailsByClassCode(any(String.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/student/C4")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void searchStudentBYNameSuccessful() throws Exception{
        List<Student> studentList = List.of(new Student("4","Bob","C4","10"));
        when(studentService.getAllStudents()).thenReturn(studentList);
        when(studentService.getStudentByName(any(String.class))).thenReturn(studentList);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/student/search?studentName=Bob")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void searchStudentBYNameBlankStudent() throws Exception{
        when(studentService.getAllStudents()).thenReturn(new ArrayList<>());
        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/student/search?studentName=")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void getStudentFromRollClassSuccessful() throws Exception {
        Student student = new Student("4","Bob","C4","10");
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenReturn(student);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/10/4")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getStudentFromRollClassException() throws Exception{
        when(studentService.getStudentFromClassRollNo(any(String.class),any(String.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/10/4")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }
}