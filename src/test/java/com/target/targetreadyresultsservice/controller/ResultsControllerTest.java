package com.target.targetreadyresultsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.targetreadyresultsservice.Dto.ResultsDto;
import com.target.targetreadyresultsservice.Dto.StudentDto;
import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
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
import static org.mockito.ArgumentMatchers.nullable;
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

    //tests for adding new result
    @Test
    void addResultsSuccessful() throws Exception{
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
    void addResultsReturnsNotFoundException() throws Exception{
        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));

        when(resultsService.addNewResult(any(Results.class))).thenThrow(NotFoundException.class);

        ResultActions response = mockMvc.perform(post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    void addResultsReturnsInvalidException() throws Exception{
        Results result = new Results("4","",
                List.of(new Marks("S999",500,500)));

        when(resultsService.addNewResult(any(Results.class))).thenThrow(InvalidValueException.class);

        ResultActions response = mockMvc.perform(post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void addResultsReturnsException() throws Exception{
        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",500,500)));

        when(resultsService.addNewResult(any(Results.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    //tests to update result
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
    void updateResultReturnsBlankValueException() throws Exception{
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
    void updateResultReturnsNotFoundException() throws Exception{
        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));
        result.setResultsCode("R4JULY2023");

        when(resultsService.updateResult(any(String.class),any(Results.class))).thenThrow(NotFoundException.class);

        ResultActions response = mockMvc.perform(put(END_POINT_PATH+"/R4JULY2023")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateResultReturnsException() throws Exception{
        Results result = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));
        result.setResultsCode("R4JULY2023");

        when(resultsService.updateResult(any(String.class),any(Results.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(put(END_POINT_PATH+"/R4JULY2023")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result)));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getStudentResultsSuccessful() throws Exception{
        ResultsDto r = new ResultsDto("R4JULY2023","4","TC420JULY2023",
                List.of(new Marks("S999",45,0)),50.0,100.0);
        List<ResultsDto> resultsDtoList = List.of(r);

        when(resultsService.getStudentResult(any(String.class),any(String.class),any(String.class))).thenReturn(resultsDtoList);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/student?rollNumber=4&className=4&academicYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getStudentResultsReturnsException() throws Exception{
        when(resultsService.getStudentResult(any(String.class),any(String.class),any(String.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/student?rollNumber=4&className=4&academicYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getStudentResultsReturnsNotFoundException() throws Exception{
        when(resultsService.getStudentResult(any(String.class),any(String.class),any(String.class))).thenReturn(new ArrayList<>());

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/student?rollNumber=4&className=4&academicYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getClassResultsSuccessful() throws Exception{
        ResultsDto r = new ResultsDto("R4JULY2023","4","TC420JULY2023",
                List.of(new Marks("S999",45,0)),50.0,100.0);
        List<ResultsDto> resultsDtoList = List.of(r);
        when(resultsService.getClassResult(any(String.class),any(String.class))).thenReturn(resultsDtoList);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/classResults?className=4&academicYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getClassResultsReturnsInvalidValueException() throws Exception{
        when(resultsService.getClassResult(any(String.class),any(String.class))).thenThrow(InvalidValueException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/classResults?className=4&academicYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getClassResultsReturnsException() throws Exception{
        when(resultsService.getClassResult(any(String.class),any(String.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/classResults?className=4&academicYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getClassTestResultsSuccessful() throws Exception{
        ResultsDto r = new ResultsDto("R4JULY2023","4","TC420JULY2023",
                List.of(new Marks("S999",45,0)),50.0,100.0);
        List<ResultsDto> resultsDtoList = List.of(r);
        when(resultsService.getClassResult(any(String.class),any(String.class))).thenReturn(resultsDtoList);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/classTest?classCode=C4" +
                "&academicYear=2023-2024&scheduleCode=TC420JULY2023")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void getClassTestResultsReturnsBlankValueException() throws Exception{
        when(resultsService.getClassTestResults(any(String.class),any(String.class),any(String.class))).thenThrow(BlankValueException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/classTest?classCode=C4" +
                "&academicYear=2023-2024&scheduleCode=TC420JULY2023")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getClassTestResultsReturnsException() throws Exception{
        when(resultsService.getClassTestResults(any(String.class),any(String.class),any(String.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/classTest?classCode=C4" +
                "&academicYear=2023-2024&scheduleCode=TC420JULY2023")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteResultsSuccessful() throws Exception{
        Results r = new Results("4","TC420JULY2023",
                List.of(new Marks("S999",45,0)));
        r.setResultsCode("R4JULY2023");
        when(resultsService.deleteResult(any(String.class))).thenReturn(r);

        ResultActions response = mockMvc.perform(delete(END_POINT_PATH+"/delete/R4JULY2023")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteResultsReturnsNotFoundException() throws Exception{
        when(resultsService.deleteResult(any(String.class))).thenThrow(NotFoundException.class);

        ResultActions response = mockMvc.perform(delete(END_POINT_PATH+"/delete/R4JULY2023")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteResultsReturnsException() throws Exception{
        when(resultsService.deleteResult(any(String.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(delete(END_POINT_PATH+"/delete/R4JULY2023")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAvgInternalForSubjectSuccessful() throws Exception{
        when(resultsService.getAverageForSubject(any(String.class),any(String.class),any(String.class),
                any(String.class))).thenReturn(45.0);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/avgInternal?rollNumber=4&className=4" +
                "&acYear=2023-2024&subjectCode=S999")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAvgInternalForSubjectReturnsInvalidValueException() throws Exception{
        when(resultsService.getAverageForSubject(any(String.class),any(String.class),any(String.class),
                any(String.class))).thenThrow(InvalidValueException.class);
        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/avgInternal?rollNumber=4&className=4" +
                "&acYear=2023-2024&subjectCode=S999")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void getAvgInternalForSubjectReturnsNotFoundException() throws Exception{
        when(resultsService.getAverageForSubject(any(String.class),any(String.class),any(String.class),
                any(String.class))).thenThrow(NotFoundException.class);
        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/avgInternal?rollNumber=4&className=4" +
                "&acYear=2023-2024&subjectCode=S999")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void getAvgInternalForSubjectReturnsException() throws Exception{
        when(resultsService.getAverageForSubject(any(String.class),any(String.class),any(String.class),
                any(String.class))).thenThrow(RuntimeException.class);
        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/avgInternal?rollNumber=4&className=4" +
                "&acYear=2023-2024&subjectCode=S999")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void getStudentTestResultSuccessful() throws Exception{
        ResultsDto r = new ResultsDto("R4JULY2023","4","TC420JULY2023",
                List.of(new Marks("S999",45,0)),50.0,100.0);
        when(resultsService.getStudentTestResult(any(String.class),any(String.class),
                any(String.class),any(String.class))).thenReturn(r);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/studentTestResult?className=4" +
                "&academicYear=2023-2024&scheduleName=TC420JULY2023&rollNumber=4")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getStudentTestResultReturnsNotFoundException() throws Exception{
        when(resultsService.getStudentTestResult(any(String.class),any(String.class),
                any(String.class),any(String.class))).thenReturn(null);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/studentTestResult?className=4" +
                "&academicYear=2023-2024&scheduleName=TC420JULY2023&rollNumber=4")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getStudentTestResultReturnsException() throws Exception{
        when(resultsService.getStudentTestResult(any(String.class),any(String.class),
                any(String.class),any(String.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/studentTestResult?className=4" +
                "&academicYear=2023-2024&scheduleName=TC420JULY2023&rollNumber=4")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getLeaderboardSuccessful() throws Exception{
        List<StudentDto> studentDtoList = List.of(new StudentDto("8","C4","15","Carl",100.0),
                new StudentDto("6","C4","12","Ann",93.0),
                new StudentDto("4","C4","10","Bob",89.0),
                new StudentDto("7","C4","30","Rob",87.0),
                new StudentDto("5","C4","1","Alice",80.0));

        when(resultsService.getLeaderboard(any(String.class),any(String.class))).thenReturn(studentDtoList);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/leaderboard?className=4&academicYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void getLeaderboardReturnsNotFoundException() throws Exception{
        when(resultsService.getLeaderboard(any(String.class),any(String.class))).thenThrow(NotFoundException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/leaderboard?className=4&academicYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getLeaderboardReturnsException() throws Exception{
        when(resultsService.getLeaderboard(any(String.class),any(String.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/leaderboard?className=4&academicYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getResultPercentageOfStudentSuccessful() throws Exception{
        when(resultsService.getResultPercentage(any(String.class),any(String.class),any(String.class))).thenReturn(45.0);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/percentage?rollNumber=10&className=4&acYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getResultPercentageOfStudentReturnsNotFound() throws Exception{
        when(resultsService.getResultPercentage(any(String.class),any(String.class),any(String.class))).thenThrow(NotFoundException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/percentage?rollNumber=10&className=4&acYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getResultPercentageOfStudentReturnsInvalidValue() throws Exception{
        when(resultsService.getResultPercentage(any(String.class),any(String.class),any(String.class))).thenThrow(InvalidValueException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/percentage?rollNumber=10&className=4&acYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getResultPercentageOfStudentReturnsException() throws Exception{
        when(resultsService.getResultPercentage(any(String.class),any(String.class),any(String.class))).thenThrow(RuntimeException.class);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/percentage?rollNumber=10&className=4&acYear=2023-2024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }
}