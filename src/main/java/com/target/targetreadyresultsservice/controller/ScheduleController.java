package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.model.SubjectSchedule;
import com.target.targetreadyresultsservice.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results/v1/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<String> addNewSchedule(@RequestBody Schedule schedule){
        try{
            scheduleService.addNewSchedule(schedule);
            return new ResponseEntity<>("Successful", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }
}
