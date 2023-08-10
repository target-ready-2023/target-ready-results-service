package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.Dto.ResultsDto;
import com.target.targetreadyresultsservice.Dto.StudentDto;
import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.model.Results;
import com.target.targetreadyresultsservice.service.ResultsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<ResultsDto>> getClassResults(
            @RequestParam("className") String className,
            @RequestParam("academicYear") String acYear

    ){
        try{
            List<ResultsDto> classResults = resultsService.getClassResult(className, acYear);
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
    public ResponseEntity<List<ResultsDto>> getClassTestResults(
            @RequestParam("classCode") String classCode,
            @RequestParam("academicYear") String acYear,
             @RequestParam("scheduleCode") String scCode
    ){
        try{
            List<ResultsDto> classTestResults = resultsService.getClassTestResults(className, acYear,scName);
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
    public ResponseEntity<Results> addResults(@RequestBody @Valid Results result){
        try{
            Results newResult = resultsService.addNewResult(result);
            log.info("Result added successfully");
            return new ResponseEntity<>(newResult,HttpStatus.CREATED);
        }catch (NotFoundException e){
            log.info("Exception occurred - NotFoundException - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (BlankValueException | InvalidValueException e){
            log.info("Exception occurred due to values provided - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    //update a result
    @PutMapping("/{resultCode}")
    public ResponseEntity<Optional<Results>> updateResult(@PathVariable String resultCode, @RequestBody Results result){
        try{
            Optional<Results> updatedResult = resultsService.updateResult(resultCode,result);
            log.info("Result updated successfully");
            return new ResponseEntity<>(updatedResult, HttpStatus.OK);
        }catch (NotFoundException e){
            log.info("Update failed. Throws NotFoundException - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (BlankValueException | InvalidValueException e){
            log.info("Exception occurred due to the values provided - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }catch (Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity("Update failed! Please try again",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //delete result
    @DeleteMapping("/delete/{resultCode}")
    public ResponseEntity<Results> deleteResults(@PathVariable String resultCode){
        try{
            Results r = resultsService.deleteResult(resultCode);
            log.info("Result - {} - deleted successfully",r);
            return new ResponseEntity<>(r,HttpStatus.OK);
        }
        catch(NotFoundException e){
            log.info("Exception occurred - NotFoundException - {}",e.getMessage());
            return new ResponseEntity("Result does not Exists",HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity("Deletion Failed",HttpStatus.EXPECTATION_FAILED);
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
        }catch (NotFoundException e){
            log.info("Exception occurred - NotFoundException - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (BlankValueException | InvalidValueException e) {
            log.info("Exception occurred due to the values provided - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }catch (Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity("Action failed!",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //get the final exam result percentage of a student in an academic year
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
        }catch (NotFoundException e){
            log.info("Exception occurred - NotFoundException - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (InvalidValueException e){
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
    public ResponseEntity<List<ResultsDto>> getStudentResults(
            @RequestParam("rollNumber") String rollNumber,
            @RequestParam("className") String className,
            @RequestParam("academicYear") String acYear
            ){
        try{
            List<ResultsDto> resultsList = resultsService.getStudentResult(rollNumber,className,acYear);
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
    public ResponseEntity<ResultsDto> getStudentTestResult(
            @RequestParam("className") String className,
            @RequestParam("academicYear") String acYear,
            @RequestParam("scheduleName") String scName,
            @RequestParam("rollNumber")  String rollNo
    ){
        try{
            ResultsDto studentResult = resultsService.getStudentTestResult(className,acYear,scName, rollNo);
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

    //get the top 5 students from a class in an academic year
    @GetMapping("/leaderboard")
    public ResponseEntity<List<StudentDto>> getLeaderboard(
            @RequestParam("className") String className,
            @RequestParam("academicYear") String acYear
    ){
        try{
            List<StudentDto> rankList = resultsService.getLeaderboard(className,acYear);
            log.info("Rank List with top 5 retrieved successfully - {}",rankList);
            return new ResponseEntity<>(rankList,HttpStatus.OK);
        } catch(NotFoundException e){
            log.info("Exception occurred - NotFoundException - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch(Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }
    //to get the Topper of each class
    @GetMapping("/schoolToppers")
    public  ResponseEntity<List<StudentDto>> getToppersList(
            @RequestParam("academicYear") String acYear
    ){
        try{
            List<StudentDto> toppersList = resultsService.getToppersList(acYear);
            log.info("List of Toppers of each class retrieved successfully- {}",toppersList);
            return new ResponseEntity<>(toppersList,HttpStatus.OK);
        }
        catch(NotFoundException e){
            log.info("Exception occurred - NotFoundException - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch(Exception e){
            log.info("Exception occurred - {}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }
}



