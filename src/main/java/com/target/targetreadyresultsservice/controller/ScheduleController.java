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
import java.util.Optional;

@CrossOrigin(origins = "*")
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
    public ResponseEntity<List<Schedule>> getAllSchedule(){
        List<Schedule> scheduleList = scheduleService.findAll();
        try{
            if (scheduleList.isEmpty()) {
                log.info("No schedules found. Exception occurred");
                throw new NotFoundException("No schedules found");
            }
            log.info("All schedules retrieved successfully - {}",scheduleList);
            return new ResponseEntity<>(scheduleList, HttpStatus.OK);
        }
        catch (NotFoundException e){
            log.info("exception occurred - NotFoundException {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Action failed",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get all schedules by academic year
    @GetMapping("/year")
    public ResponseEntity<List<Schedule>> getAllScheduleByAcYear(
            @RequestParam("acYear") String acYear
    ){
        try{
            List<Schedule> scheduleList = scheduleService.getScheduleByYear(acYear);
            if(scheduleList.isEmpty()){
                throw new NotFoundException("No schedules found");
            }
            log.info("All schedules for the academic year - {} retrieved successfully as - {}",acYear,scheduleList);
            return new ResponseEntity<>(scheduleList, HttpStatus.OK);
        } catch (NotFoundException e){
            log.info("exception occurred - NotFoundException {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (NullValueException | BlankValueException e){
            log.info("exception occurred due to values provided - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e){
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
            log.info("Schedule retrieved successfully - {}",scheduleInfo);
            return new ResponseEntity<>(scheduleInfo, HttpStatus.OK);
        }
        catch (NotFoundException e){
            log.info("exception occurred - NotFoundException {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Action failed! An error occurred",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get active schedule by classCode
    @GetMapping("/{classCode}/active")
    public ResponseEntity<List<Schedule>> getActiveSchedule(
               @PathVariable String classCode
       ){
        try{
            List<Schedule> activeScheduleList = scheduleService.getActiveSchedule(classCode);
            log.info("Active schedules for a class retrieved successfully - {}",activeScheduleList);
            return new ResponseEntity<>(activeScheduleList, HttpStatus.OK);
        }
        catch (NullValueException e){
            log.info("exception occurred - NullValueException - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            log.info("exception occurred {}", e.getMessage());
            return new ResponseEntity("Action failed! An error occurred",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get all schedules by classCode in an academic year(includes both active and inactive)
    @GetMapping("/{classCode}/{acYear}/all")
    public ResponseEntity<List<Schedule>> getScheduleByClass(
            @PathVariable String classCode,
            @PathVariable String acYear
    ){
        try{
            List<Schedule> scheduleList = scheduleService.getScheduleByClass(classCode,acYear);
            log.info("schedule list retrieved successfully - {}",scheduleList);
            return new ResponseEntity<>(scheduleList,HttpStatus.OK);
        }
        catch(NotFoundException e){
            log.info("exception occurred  - NotFoundException - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.info("exception occurred - {}", e.getMessage());
            return new ResponseEntity("Active failed!",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get list of schedule names for a class in an academic year
    @GetMapping("/scheduleNames")
    private ResponseEntity<List<String>> getScheduleNamesForClass(
            @RequestParam("className") String className,
            @RequestParam("acYear") String acYear
    ){
        try{
            List<String> scheduleNameList = scheduleService.getScheduleNamesForClass(className,acYear);
            if(scheduleNameList.isEmpty()){
                log.info("scheduleNameList is empty. Throws NotFoundException");
                throw new NotFoundException("No schedules found");
            }
            log.info("Schedule name list found successfully as - {}",scheduleNameList);
            return new ResponseEntity<>(scheduleNameList,HttpStatus.OK);
        }catch (NotFoundException e){
            log.info("No schedules found");
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
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
            log.info("Schedule retrieved successfully - {}",schedule);
            return new ResponseEntity<>(schedule,HttpStatus.OK);
        }catch (NotFoundException e){
            log.info("exception occurred  - NotFoundException - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (InvalidValueException | BlankValueException e){
            log.info("exception occurred - Invalid or Blank value provided - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e){
            log.info("exception occurred - {}", e.getMessage());
            return new ResponseEntity("Action failed!",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //add new schedule
    @PostMapping
    public ResponseEntity<Schedule> addNewSchedule(@RequestBody @Valid Schedule schedule){
        try{
            Schedule newSchedule = scheduleService.addNewSchedule(schedule);
            log.info("Schedule added successfully");
            return new ResponseEntity<>(newSchedule,HttpStatus.CREATED);
        } catch (BlankValueException | NullValueException e){
           log.info("exception occurred - BLankValue Exception - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        } catch (NotFoundException e){
            log.info("exception occurred - NotFoundException - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (Exception e){
            log.info("exception occurred - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    //delete a schedule
    @DeleteMapping("{scheduleCode}")
    public ResponseEntity<Schedule> deleteSchedule(@PathVariable String scheduleCode){
        try {
            Schedule schedule = scheduleService.deleteSchedule(scheduleCode);
            log.info("Schedule deleted successfully");
            return new ResponseEntity<>(schedule,HttpStatus.OK);
        }
      catch (NotFoundException e){
            log.info("exception occurred - NotFoundException - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.info("exception occurred - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }

    }

    //update a schedule
    @PutMapping("/{scheduleCode}")
    public ResponseEntity<Optional<Schedule>> updateSchedule(@PathVariable String scheduleCode, @RequestBody Schedule schedule){
        try{
            Optional<Schedule> updatedSchedule = scheduleService.updateSchedule(scheduleCode, schedule);
            log.info("Schedule updated successfully");
            return new ResponseEntity<>(updatedSchedule,HttpStatus.OK);
        }catch (BlankValueException | InvalidValueException e){
            log.info("exception occurred - Blank or Invalid value provided - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e){
            log.info("exception occurred - {}", e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get the list of acYears for a class
    @GetMapping("/{classCode}/acYears")
    public ResponseEntity<List<String>> getScheduleAcYearsForClass(@PathVariable String classCode){
        try{
            List<String> acYears = scheduleService.getScheduleAcYearsForClass(classCode);
            if(acYears.isEmpty()){
                log.info("acYears is empty - Throws NotFoundException");
                throw new NotFoundException("No schedules found for the given class");
            }
            log.info("acYears found as - {}",acYears);
            return new ResponseEntity<>(acYears,HttpStatus.OK);
        }catch (NotFoundException e){
            log.info("acYears not found - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (BlankValueException e){
            log.info("Exception due to the values provided - Throws BlankValueException");
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
        catch (Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity("Action failed!",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get list of schedule names for a class in the current academic year
    @GetMapping("/current/scNames")
    private ResponseEntity<List<String>> getScheduleNamesForCurrentAcYear(
            @RequestParam("classCode") String classCode
    ){
        try{
            List<String> scheduleNameList = scheduleService.getScheduleNamesForCurrentAcYear(classCode);
            if(scheduleNameList.isEmpty()){
                log.info("scheduleNameList is empty. Throws NotFoundException");
                throw new NotFoundException("No schedules found");
            }
            log.info("Schedule name list found successfully as - {}",scheduleNameList);
            return new ResponseEntity<>(scheduleNameList,HttpStatus.OK);
        }catch (NotFoundException e){
            log.info("No schedules found");
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (BlankValueException e){
            log.info("No class code provided");
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }
}
