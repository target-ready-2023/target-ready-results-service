package com.target.targetreadyresultsservice.controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Executable;

@RestController
@RequestMapping("schedule/v1")
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

    @DeleteMapping
    public ResponseEntity<String> deleteSchedule(@RequestParam("scheduleCode") String scheduleCode){
        String response = scheduleService.deleteSchedule(scheduleCode);
        if(response == null){
            return new ResponseEntity<>("Schedule not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
    }

    @PutMapping("/{scheduleCode}")
    public ResponseEntity<String> updateSchedule(@PathVariable String scheduleCode, @RequestBody Schedule schedule){
        try{
            scheduleService.updateSchedule(scheduleCode, schedule);
            return new ResponseEntity<>("Successful",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Error",HttpStatus.EXPECTATION_FAILED);
        }
    }
}
