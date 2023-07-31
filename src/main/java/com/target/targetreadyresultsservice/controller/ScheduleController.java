package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.Exception.NullValueException;
import com.target.targetreadyresultsservice.model.Schedule;
import com.target.targetreadyresultsservice.service.ScheduleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/schedule/v1")
public class ScheduleController {
    @Autowired
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    //get all schedules
    @GetMapping("/all")
    public ResponseEntity<List<Schedule>> getallSchedule(){
        List<Schedule> scheduleList = scheduleService.findAll();
        try{
            if (scheduleList.isEmpty()) {
                throw new NotFoundException("No schedules found");
            }
            return new ResponseEntity<>(scheduleList, HttpStatus.OK);
        }
        catch (NotFoundException e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
        catch (Exception e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Action failed",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get schedule by scheduleCode
    @GetMapping("/{scheduleCode}")
    public ResponseEntity<Schedule> getSchedule(
            @PathVariable String scheduleCode
    ){
        try {
            Schedule scheduleInfo = scheduleService.getScheduleDetails(scheduleCode);
            return new ResponseEntity<>(scheduleInfo, HttpStatus.OK);
        }
        catch (NotFoundException e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
        catch (Exception e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Action failed! An error occurred",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get active schedule by classCode
    @GetMapping("/{classCode}/active")
    public ResponseEntity<List<Schedule>> getactiveSchedule(
               @PathVariable String classCode
       ){
        try{
            List<Schedule> activeScheduleList = scheduleService.getactiveSchedule(classCode);
            return new ResponseEntity<>(activeScheduleList, HttpStatus.OK);
        }
        catch (NullValueException e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Action failed! An error occurred",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get all schedules by classCode
    @GetMapping("/{classCode}/all")
    public ResponseEntity<List<Schedule>> getScheduleByClass(@PathVariable String classCode){
        try{
            List<Schedule> scheduleList = scheduleService.getScheduleByClass(classCode);
            return new ResponseEntity<>(scheduleList,HttpStatus.OK);
        }
        catch(NullValueException e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Active failed!",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get a schedule from schedule name, class name and academic year
    @GetMapping("/viewSchedule")
    public ResponseEntity<Schedule> getScheduleForResult(
            @RequestParam("scheduleName") String scheduleName,
            @RequestParam("className") String className,
            @RequestParam("acYear") String acYear
    ){
        try{
            Schedule schedule = scheduleService.getScheduleForResult(scheduleName,className,acYear);
            return new ResponseEntity<>(schedule,HttpStatus.FOUND);
        }catch (NotFoundException e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidValueException | BlankValueException e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Action failed!",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //add new schedule
    @PostMapping
    public ResponseEntity<String> addNewSchedule(@RequestBody @Valid Schedule schedule){
        try{
            scheduleService.addNewSchedule(schedule);
            return new ResponseEntity<>("Successful", HttpStatus.CREATED);
        }
       catch (BlankValueException e){
           log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
       }catch (NullValueException e){
            log.info("exception occurred {}", e.getMessage());
           return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
       }catch (NotFoundException e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    //delete a schedule
    @DeleteMapping("{scheduleCode}")
    public ResponseEntity<String> deleteSchedule(@PathVariable String scheduleCode){
        try {
            scheduleService.deleteSchedule(scheduleCode);
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }
      catch (NotFoundException e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }

    }

    //update a schedule
    @PutMapping("/{scheduleCode}")
    public ResponseEntity<String> updateSchedule(@PathVariable String scheduleCode, @RequestBody Schedule schedule){
        try{
            scheduleService.updateSchedule(scheduleCode, schedule);
            return new ResponseEntity<>("Successful",HttpStatus.OK);
        }catch (BlankValueException | InvalidValueException e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }
}
