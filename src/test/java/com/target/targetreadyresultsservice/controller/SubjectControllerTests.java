package com.target.targetreadyresultsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.targetreadyresultsservice.model.Subject;
import com.target.targetreadyresultsservice.repository.SubjectRepository;
import com.target.targetreadyresultsservice.service.SubjectService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(controllers = SubjectController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class SubjectControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubjectService subjectService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
   public void SubjectController_setSubjectDetails_ReturnCreated() throws Exception
    {
        Subject sub=new Subject();
        sub.setClassCode("S-PhC1");
        sub.setSubjectName("Physics");
        sub.setCredits(10);
        sub.setClassCode("C1");
        sub.setMaxTestMarks(20);
        sub.setMaxExamMarks(80);
        given(subjectService.addSubject(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));
        ResultActions response=mockMvc.perform(post("/subjects/v1/subject")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sub)));
        response.andExpect(MockMvcResultMatchers.status()
                .isCreated())
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void SubjectController_setSubjectDetails_ReturnException() throws Exception
    {
        Subject sub=new Subject();
       sub.setClassCode("S-PhC1");
        sub.setSubjectName("Physics");
        sub.setCredits(10);
        sub.setClassCode("C1");
        sub.setMaxTestMarks(20);
        sub.setMaxExamMarks(80);
        when(subjectService.addSubject(any(Subject.class))).thenThrow(RuntimeException.class);
        ResultActions response=mockMvc.perform(post("/subjects/v1/subject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sub)));
        response.andExpect(MockMvcResultMatchers.status()
                .isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void SubjectController_setSubjectDetails_ReturnFailed() throws Exception
    {
        Subject sub=new Subject(null,null,null,null,null,null);
        ResultActions response=mockMvc.perform(post("/subjects/v1/subject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sub))
        );
        response.andExpect(MockMvcResultMatchers.status().is4xxClientError()).andDo(MockMvcResultHandlers.print());
    }
    @Test public void SubjectController_getAllSubjects_ReturnOk() throws Exception
    {
        Subject sub=new Subject();
        sub.setClassCode("S-PhC1");
        sub.setSubjectName("Physics");
        sub.setCredits(10);
        sub.setClassCode("C1");
        sub.setMaxTestMarks(20);
        sub.setMaxExamMarks(80);
  when(subjectService.getSubjects()).thenReturn(List.of(sub));
  ResultActions response=mockMvc.perform(get("/subjects/v1/subject")
          .contentType(MediaType.APPLICATION_JSON));
  response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }
    @Test public void SubjectController_getAllSubjects_ReturnFailed() throws Exception
    {
        when(subjectService.getSubjects()).thenThrow(RuntimeException.class);
        ResultActions response=mockMvc.perform(get("/subjects/v1/subject")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed()).andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void SubjectController_getSubjectById_ReturnOk() throws Exception
    {
        Subject sub=new Subject();
        sub.setSubjectCode("S-PhC1");
        sub.setSubjectName("Physics");
        sub.setCredits(10);
        sub.setClassCode("C1");
        sub.setMaxTestMarks(20);
        sub.setMaxExamMarks(80);
        when(subjectService.getSubjectById(anyString())).thenReturn(Optional.of(sub));
        ResultActions response=mockMvc.perform(get("/subjects/v1/subject/S-PhC1")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
        //.andExpect(MockMvcResultMatchers.jsonPath("$.subjectName", CoreMatchers.is(sub.getSubjectName())));
    }
    @Test
    public void SubjectController_getSubjectById_ReturnFailed() throws Exception
    {
        when(subjectService.getSubjectById(anyString())).thenThrow(RuntimeException.class);
        ResultActions response=mockMvc.perform(get("/subjects/v1/subject/S-PhC1")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed()).andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void SubjectController_updateSubjectById_ReturnOk() throws Exception
    {
        Subject newsub=new Subject();
        newsub.setSubjectCode("S-PhC1");
        newsub.setSubjectName("Physics");
        newsub.setCredits(20);
        newsub.setClassCode("C1");
        newsub.setMaxTestMarks(20);
        newsub.setMaxExamMarks(80);
        when(subjectService.updateSubject("S-PhC1",newsub)).thenReturn(newsub);
        ResultActions response=mockMvc.perform(put("/subjects/v1/subject/S-PhC1")
                .contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(newsub)));
        response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void SubjectController_updateSubjectById_ReturnFailed() throws Exception
    {
        Subject newsub=new Subject();
       newsub.setSubjectCode("S-PhC1");
        newsub.setSubjectName("Physics");
        newsub.setCredits(20);
        newsub.setClassCode("C1");
        newsub.setMaxTestMarks(20);
        newsub.setMaxExamMarks(80);
        when(subjectService.updateSubject(anyString(),any(Subject.class))).thenThrow(RuntimeException.class);
        ResultActions response=mockMvc.perform(put("/subjects/v1/subject/S-EnC1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsub))
                );
        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void SubjectController_deleteSubjectById_ReturnOk() throws Exception
    {
        Subject sub=new Subject();
        sub.setSubjectCode("S-PhC1");
        sub.setSubjectName("Physics");
        sub.setCredits(10);
        sub.setClassCode("C1");
        sub.setMaxTestMarks(20);
        sub.setMaxExamMarks(80);
        when(subjectService.deleteSubject("S-PhC1")).thenReturn("Successfully deleted");
        ResultActions response=mockMvc.perform(delete("/subjects/v1/subject/S-PhC1")
                .contentType(MediaType.APPLICATION_JSON)
                );
        response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void SubjectController_deleteSubjectById_ReturnFailed() throws Exception
    {

        when(subjectService.deleteSubject("S-PhC1")).thenThrow(RuntimeException.class);
        ResultActions response=mockMvc.perform(delete("/subjects/v1/subject/S-PhC1")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed()).andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void SubjectController_searchSubject_ReturnSubjects() throws Exception
    {
        Subject sub=new Subject();
        sub.setSubjectCode("S-PhC1");
        sub.setSubjectName("Physics");
        sub.setCredits(10);
        sub.setClassCode("C1");
        sub.setMaxTestMarks(20);
        sub.setMaxExamMarks(80);
        when(subjectService.searchSubjectsByFilters("Physics")).thenReturn(List.of(sub));
        ResultActions response=mockMvc.perform(get("/subjects/v1/search").param("subjectName","Physics")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void SubjectController_searchSubject_NoName_ReturnAllSubjects() throws Exception
    {
        Subject sub=new Subject();
        sub.setSubjectCode("S-SoC1");
        sub.setSubjectName("Social");
        sub.setCredits(10);
        sub.setClassCode("C1");
        sub.setMaxTestMarks(20);
        sub.setMaxExamMarks(80);
        when(subjectService.getSubjects()).thenReturn(List.of(sub));
        ResultActions response=mockMvc.perform(get("/subjects/v1/search").param("subjectName","")
                .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void SubjectController_searchSubject_ReturnFailed() throws Exception
    {
        Subject sub=new Subject();
        sub.setSubjectCode("S-PhC1");
        sub.setSubjectName("Physics");
        sub.setCredits(10);
        sub.setClassCode("C1");
        sub.setMaxTestMarks(20);
        sub.setMaxExamMarks(80);
        when(subjectService.searchSubjectsByFilters("Physics")).thenThrow(RuntimeException.class);
        ResultActions response=mockMvc.perform(get("/subjects/v1/search").param("subjectName","Physics")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed()).andDo(MockMvcResultHandlers.print());

    }
}

