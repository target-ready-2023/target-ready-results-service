package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


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
    public ResponseEntity<String> getSchedule(
            @PathVariable String scheduleCode
//            @RequestParam ("scheduleCode") String scheduleCode
    ){
        try {
            Optional<Schedule> scheduleInfo = scheduleService.getScheduleDetails(scheduleCode);
            return scheduleInfo.map(
                    schedule -> new ResponseEntity<>("Found : " + schedule.toString(), HttpStatus.OK)
            ).orElseGet(
                    () -> new ResponseEntity<>("Not Found : ", HttpStatus.NOT_FOUND)
            );
        }
            catch (Exception e) {
//                log.info("exception occurred {}", e.getMessage());
                return new ResponseEntity<>("Error", HttpStatus.EXPECTATION_FAILED);
            }
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
