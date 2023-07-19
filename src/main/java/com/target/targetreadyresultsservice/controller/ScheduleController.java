package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("schedule/v1")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Schedule>> getallSchedule(){
        List<Schedule> scheduleList = scheduleService.findAll();
        if (scheduleList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scheduleList, HttpStatus.OK);
    }

    @GetMapping("/{scheduleCode}")
    public ResponseEntity<Schedule> getSchedule(
            @PathVariable String scheduleCode
    ){
        try {
            Schedule scheduleInfo = scheduleService.getScheduleDetails(scheduleCode);
            if(scheduleInfo== null)
                return new ResponseEntity<>( HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(scheduleInfo, HttpStatus.OK);
        }
            catch (Exception e) {
                return new ResponseEntity<>( HttpStatus.EXPECTATION_FAILED);
            }
        }

       @GetMapping("/{classCode}/active")
       public ResponseEntity<List<Schedule>> getactiveSchedule(
               @PathVariable String classCode
       ){
        try{
            List<Schedule> activeScheduleList = scheduleService.getactiveSchedule(classCode);
            if (activeScheduleList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else {
                return new ResponseEntity<>(activeScheduleList, HttpStatus.OK);
            }
        }
    catch(Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
       }


    @PostMapping
    public ResponseEntity<String> addNewSchedule(@RequestBody Schedule schedule){
        try{
            scheduleService.addNewSchedule(schedule);
            return new ResponseEntity<>("Successful", HttpStatus.OK);
        }catch (BlankValueException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("{scheduleCode}")
    public ResponseEntity<String> deleteSchedule(@PathVariable String scheduleCode){
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
