package com.target.targetreadyresultsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.Exception.NullValueException;
import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.model.SubjectSchedule;
import com.target.targetreadyresultsservice.repository.ScheduleRepository;
import com.target.targetreadyresultsservice.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = ScheduleController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ScheduleControllerTest {
    private static final String END_POINT_PATH = "/schedule/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;
    @MockBean
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void addNewScheduleReturnsCreated() throws Exception {

        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023, 7, 10),
                LocalTime.of(10, 00),
                50, true));
        Schedule schedule = new Schedule("C99", subjectScheduleList, "Test", "Class Test 1", true);

        given(scheduleService.addNewSchedule(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post(END_POINT_PATH)
              .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(schedule)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void addNewScheduleReturnsException() throws Exception{
        Schedule schedule = new Schedule(null," ",Collections.EMPTY_LIST," "," ",true);
        when(scheduleService.addNewSchedule(any(Schedule.class))).thenThrow(BlankValueException.class);

        ResultActions response = mockMvc.perform(post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(schedule)));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAllSchedulesSuccessful() throws Exception{
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023, 7, 10),
                LocalTime.of(10, 00),
                50, true));
        List<Schedule> scheduleList = List.of(new Schedule("C99", subjectScheduleList,
                "Test", "Class Test 1", true));

        when(scheduleService.findAll()).thenReturn(scheduleList);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAllSchedulesReturnException() throws Exception{
        List<Schedule> scheduleList = new ArrayList<>();
        when(scheduleService.findAll()).thenReturn(scheduleList);
        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getScheduleByIdSuccessful() throws Exception {
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023, 7, 10),
                LocalTime.of(10, 00),
                50, true));
        Schedule schedule = new Schedule("TC9910JULY2023","C99", subjectScheduleList,
                "Test", "Class Test 1", true);
        when(scheduleService.getScheduleDetails(any(String.class))).thenReturn(schedule);

        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/TC9910JULY2023")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getScheduleByIdReturnsException() throws Exception{
        when(scheduleService.getScheduleDetails(any(String.class)))
                .thenThrow(NotFoundException.class);
        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/TC9910JULY2023")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getActiveSchedulesByClassSuccessful() throws Exception{
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023, 7, 10),
                LocalTime.of(10, 00),
                50, true));
        List<Schedule> scheduleList = List.of(new Schedule("TC9910JULY2023","C99", subjectScheduleList,
                "Test", "Class Test 1", true));
        when(scheduleService.getactiveSchedule(any(String.class))).thenReturn(scheduleList);
        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/C99/active")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void getActiveSchedulesByClassReturnsException() throws Exception{
        when(scheduleService.getactiveSchedule(any(String.class))).thenThrow(NullValueException.class);
        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/C99/active")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getSchedulesBYClassSuccessful() throws Exception{
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023, 7, 10),
                LocalTime.of(10, 00),
                50, true));
        List<Schedule> scheduleList = List.of(new Schedule("TC9910JULY2023","C99", subjectScheduleList,
                "Test", "Class Test 1", true));
        when(scheduleService.getScheduleByClass(any(String.class))).thenReturn(scheduleList);
        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/C99/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getSchedulesByClassReturnsException() throws Exception{
        when(scheduleService.getScheduleByClass(any(String.class))).thenThrow(NullValueException.class);
        ResultActions response = mockMvc.perform(get(END_POINT_PATH+"/C99/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateScheduleSuccessful() throws Exception{
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023, 7, 10),
                LocalTime.of(10, 00),
                50, true));
        Schedule schedule = new Schedule("TC9910JULY2023","C99", subjectScheduleList,
                "Test", "Class Test 1", true);

        when(scheduleService.updateSchedule(any(String.class),any(Schedule.class))).thenReturn(Optional.ofNullable(schedule));

        ResultActions response = mockMvc.perform(put(END_POINT_PATH+"/TC9910JULY2023")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(schedule)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateScheduleReturnsException() throws Exception{
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023, 7, 16),
                LocalTime.of(23, 00),
                50, true));
        Schedule schedule = new Schedule("",subjectScheduleList,"","",true);
        when(scheduleService.updateSchedule(any(String.class),any(Schedule.class))).thenThrow(BlankValueException.class);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(END_POINT_PATH+"/TC9910JULY2023")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(schedule));

        mockMvc.perform(mockRequest)
                .andExpect(status().isExpectationFailed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteScheduleSuccessful() throws Exception{
        List<SubjectSchedule> subjectScheduleList = List.of(new SubjectSchedule("S999",
                LocalDate.of(2023, 7, 10),
                LocalTime.of(10, 00),
                50, true));
        Schedule schedule = new Schedule("TC9910JULY2023","C99", subjectScheduleList,
                "Test", "Class Test 1", true);

        when(scheduleService.deleteSchedule(any(String.class))).thenReturn(schedule.toString());

        ResultActions response = mockMvc.perform(delete(END_POINT_PATH+"/TC9910JULY2023")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteScheduleReturnsException() throws Exception{
        when(scheduleService.deleteSchedule(any(String.class))).thenThrow(NotFoundException.class);

        ResultActions response = mockMvc.perform(delete(END_POINT_PATH+"/TC9910JULY2023")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}