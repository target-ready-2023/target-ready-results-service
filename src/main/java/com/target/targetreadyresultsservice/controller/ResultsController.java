package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.model.Results;
import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.service.ResultsService;
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
@RequestMapping("results/v1")
public class ResultsController {
    @Autowired
    private final ResultsService resultsService;

    public ResultsController(ResultsService resultsService) {
        this.resultsService = resultsService;
    }

    private static final Logger log = LoggerFactory.getLogger(ResultsController.class);

    //get class results of an academic year i.e., 1st June of (year) to 31st March of (year+1)
    @GetMapping("/classResults")
    public ResponseEntity<List<Results>> getClassResults(
            @RequestParam("className") String className,
            @RequestParam("academicYear") String acYear

    ){
        try{
            List<Results> classResults = resultsService.getClassResult(className, acYear);
            log.info("Class Results retrieved successfully - {}",classResults);
            return new ResponseEntity<>(classResults,HttpStatus.OK);
        }
        catch(InvalidValueException | BlankValueException | NotFoundException e){
            log.info("Exception occurred due to the values provided - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
        catch(Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity("Action failed!",HttpStatus.EXPECTATION_FAILED);
        }
    }


    //get class results for a particular test (only tests, not exams or final exams)
    @GetMapping("/classTest")
    public ResponseEntity<List<Results>> getClassTestResults(
            @RequestParam("className") String className,
            @RequestParam("academicYear") String acYear,
             @RequestParam("scheduleName") String scName
    ){
        try{
            List<Results> classTestResults = resultsService.getClassTestResults(className, acYear,scName);
            log.info("Class test results retrieved successfully - {}",classTestResults);
            return new ResponseEntity<>(classTestResults,HttpStatus.OK);
        }
        catch(InvalidValueException | BlankValueException | NotFoundException e){
            log.info("Exception occurred due to values provided - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
        catch(Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity("Action Failed "+ e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    //add new results
    @PostMapping
    public ResponseEntity<String> addResults(@RequestBody @Valid Results result){
        try{
            resultsService.addNewResult(result);
            log.info("Result added successfully");
            return new ResponseEntity<>("Successful",HttpStatus.CREATED);
        }catch (NotFoundException e){
            log.info("Exception occurred - NotFoundException - {}",e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    //update a result
    @PutMapping("/{resultCode}")
    public ResponseEntity<String> updateResult(@PathVariable String resultCode, @RequestBody Results result){
        try{
            resultsService.updateResult(resultCode,result);
            log.info("Result updated successfully");
            return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
        }catch (NotFoundException | BlankValueException | InvalidValueException e){
            log.info("Exception occurred due to the values provided - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }catch (Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity("Update failed! Please try again",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //delete result
    @DeleteMapping("/delete/{resultCode}")
    public ResponseEntity<String> deleteResults(@PathVariable String resultCode){
        try{
            Results r = resultsService.deleteResult(resultCode);
            log.info("Result - {} - deleted successfully",r);
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }
        catch(NotFoundException e){
            log.info("Exception occurred - NotFoundException - {}",e.getMessage());
            return new ResponseEntity<>("Result does not Exists",HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity<>("Deletion Failed",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get the average of test marks in a subject for a student in an academic year
    @GetMapping("/avgInternal")
    public ResponseEntity<Double> getAvgInternalForSubject(
            @RequestParam("rollNumber") String rollNumber,
            @RequestParam("className") String className,
            @RequestParam("acYear") String acYear,
            @RequestParam("subjectCode") String subjectCode
    ){
        try {
            double avgInternals = resultsService.getAverageForSubject(rollNumber,className,acYear,subjectCode);
            log.info("Average found successfully for {} as {}",subjectCode,avgInternals);
            return new ResponseEntity<>(avgInternals,HttpStatus.OK);
        } catch (NotFoundException | InvalidValueException e) {
            log.info("Exception occurred due to the values provided - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }catch (Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity("Action failed!",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get the result percentage of a student in an academic year
    @GetMapping("/percentage")
    public ResponseEntity<Double> getResultPercentageOfStudent(
            @RequestParam("rollNumber") String rollNumber,
            @RequestParam("className") String className,
            @RequestParam("acYear") String acYear
    ){
        try{
            double percentage = resultsService.getResultPercentage(rollNumber,className,acYear);
            log.info("Result percentage found successfully as - {}",percentage);
            return new ResponseEntity<>(percentage,HttpStatus.OK);
        }catch (NotFoundException | InvalidValueException e){
            log.info("Exception occurred due to the values provided - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
        catch (Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity("Action failed!",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get all results of one student for an academic year (including all tests, exams and final exam)
    @GetMapping("/student")
    public ResponseEntity<List<Results>> getStudentResults(
            @RequestParam("rollNumber") String rollNumber,
            @RequestParam("className") String className,
            @RequestParam("acYear") String acYear
            ){
        try{
            List<Results> resultsList = resultsService.getStudentResult(rollNumber,className,acYear);
            if(resultsList.isEmpty()){
                throw new NotFoundException("No results found");
            }
            log.info("Results retrieved successfully - {}",resultsList);
            return new ResponseEntity<>(resultsList,HttpStatus.OK);
        }catch (NotFoundException e){
            log.info("Exception occurred - NotFoundException - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get student results for one test/exam or final exam
    @GetMapping("/studentTestResult")
    public ResponseEntity<Results> getStudentTestResult(
            @RequestParam("className") String className,
            @RequestParam("academicYear") String acYear,
            @RequestParam("scheduleName") String scName,
            @RequestParam("rollNumber")  String rollNo
    ){
        try{
            Results studentResult = resultsService.getStudentTestResult(className,acYear,scName, rollNo);
            if(studentResult==null){
                throw new NotFoundException("No results Found");
            }
            log.info("Results retrieved successfully - {}",studentResult);
            return new ResponseEntity<>(studentResult,HttpStatus.OK);
        }
        catch(NotFoundException e){
            log.info("Exception occurred - NotFoundException - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("leaderboard")
    public ResponseEntity<List<Student>> getLeaderboard(
            @RequestParam("className") String className,
            @RequestParam("academicYear") String acYear
    ){
        try{
            List<Student> rankList = resultsService.getLeaderboard(className,acYear);
        log.info("Rank List with top 5 retirved successfully - {}",rankList);
        return new ResponseEntity<>(rankList,HttpStatus.OK);
        }
        catch(NotFoundException e){
        log.info("Exception occurred - NotFoundException - {}",e.getMessage());
        return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
    }
        catch(Exception e){
        log.info("Exception occurred - {}",e.getMessage());
        return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
    }
    }
}

