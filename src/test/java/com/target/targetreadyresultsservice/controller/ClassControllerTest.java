package com.target.targetreadyresultsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.targetreadyresultsservice.Dto.ClassDto;
import com.target.targetreadyresultsservice.model.ClassLevel;
import com.target.targetreadyresultsservice.service.ClassService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ClassController.class)
public class ClassControllerTest {
    private static final String END_POINT_PATH="/classes/v1/classes";
    @MockBean
    private ClassService classService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private ClassLevel classLevel;
    private ClassDto classDto;


    @Test
    void savaClassShouldReturnCreated() throws Exception{
        classLevel = new ClassLevel("C4","4");
        given(classService.setClassLevelInfo(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(classLevel)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void savaClassShouldReturnException() throws Exception{
        classLevel = new ClassLevel(null,null);

        ResultActions response = mockMvc.perform(post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(classLevel)));

        response.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAllClassDetailsShouldReturnClassDto() throws  Exception{
        List<ClassDto> classDto = List.of(new ClassDto("C4","4",List.of("Math","Social","English")),
                                          new ClassDto("C5","5",List.of("Math","Social","Physics")) );
        when(classService.getAllClasses()).thenReturn(classDto);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAllClassDetailsShouldReturnNotFound() throws  Exception{
        List<ClassDto> classDto = null;
        when(classService.getAllClasses()).thenReturn(classDto);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getClassDetailsByIDShouldReturnClassDto() throws Exception{
        classDto = new ClassDto("C4","4",List.of("Math","Physics","Social"));
        String code="C4";
        when(classService.getClassLevelById("C4")).thenReturn(classDto);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/"+code)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getClassDetailsByIDShouldReturnNotFound() throws Exception{
        String code="C4";
        when(classService.getClassLevelById("C4")).thenReturn(classDto);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/"+code)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateClassDetailsShouldReturnUpdated() throws Exception {
        ClassLevel classLevel= new ClassLevel("C4","Four");
        ClassLevel classLevel1 = new ClassLevel();
        classLevel1.setName("Four");

        when(classService.updateClassLevelInfo("C4",classLevel1)).thenReturn(classLevel);
        ResultActions response = mockMvc.perform(put(END_POINT_PATH+"/"+"C4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(classLevel)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    void updateClassDetailsShouldReturnException() throws Exception {
        ClassLevel classLevel= new ClassLevel("C4","Four");

        when(classService.updateClassLevelInfo(any(String.class),any(ClassLevel.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(put(END_POINT_PATH+"/C4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(classLevel)));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteClassByIdShouldReturnString() throws Exception{
        classLevel = new ClassLevel("C4","4");
        String code="C4";
        when(classService.deleteClassLevelInfo("C4")).thenReturn("deleted Successfully");

        ResultActions response = mockMvc.perform(delete(END_POINT_PATH+"/"+code)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteClassByIdShouldReturnException() throws Exception{
        classLevel = new ClassLevel("C4","4");
        String code="C4";

        when(classService.deleteClassLevelInfo(anyString())).thenThrow(RuntimeException.class);
        ResultActions response = mockMvc.perform(delete(END_POINT_PATH+"/"+code)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test
    void getClassDetailsByNameShouldReturnClassDto() throws Exception{
        List<ClassDto> classDto = List.of(new ClassDto("C4","4",List.of("Math","Physics","Social")));
        when(classService.getClassLeveByName("4")).thenReturn(classDto);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("className","4"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getClassDetailsByNameShouldReturnNotFound() throws Exception{
        when(classService.getClassLeveByName("4")).thenReturn((List<ClassDto>) classDto);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/search")
                .contentType(MediaType.APPLICATION_JSON)
                        .param("className","4"));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}

