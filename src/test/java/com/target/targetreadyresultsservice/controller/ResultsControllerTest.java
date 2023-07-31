package com.target.targetreadyresultsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.model.Marks;
import com.target.targetreadyresultsservice.model.Results;
import com.target.targetreadyresultsservice.service.ResultsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ResultsController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ResultsControllerTest {

    private static final String END_POINT_PATH = "/results/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResultsService resultsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addResults() throws Exception{
        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));
        given(resultsService.addNewResult(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void addResultsReturnsException() throws Exception{
        Results result = new Results("","",
                List.of(new Marks("S999",45,0)));

        when(resultsService.addNewResult(any(Results.class))).thenThrow(BlankValueException.class);

        ResultActions response = mockMvc.perform(post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateResult() throws Exception{
        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));
        result.setResultsCode("R4JULY2023");

        when(resultsService.updateResult(any(String.class),any(Results.class))).thenReturn(Optional.of(result));

        ResultActions response = mockMvc.perform(put(END_POINT_PATH+"/R4JULY2023")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void updateResultReturnsException() throws Exception{
        Results result = new Results("","",
                List.of(new Marks("S999",45,0)));
        result.setResultsCode("");

        when(resultsService.updateResult(any(String.class),any(Results.class))).thenThrow(BlankValueException.class);

        ResultActions response = mockMvc.perform(put(END_POINT_PATH+"/R4JULY2023")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getStudentResults() throws Exception{
        Results r = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));
        r.setResultsCode("R4JULY2023");
        List<Results> resultsList = List.of(r);

        when(resultsService.getStudentResult(any(String.class),any(String.class))).thenReturn(resultsList);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/student?studentId=4&acYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getStudentResultsReturnsException() throws Exception{
        when(resultsService.getStudentResult(any(String.class),any(String.class))).thenReturn(new ArrayList<>());

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/student?studentId=4&acYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}