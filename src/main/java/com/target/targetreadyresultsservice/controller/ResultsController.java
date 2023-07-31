package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.model.Results;
import com.target.targetreadyresultsservice.model.Student;
import com.target.targetreadyresultsservice.service.ResultsService;
import com.target.targetreadyresultsservice.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("results/v1")
public class ResultsController {
    private final ResultsService resultsService;
    private final StudentService studentService;

    public ResultsController(ResultsService resultsService, StudentService studentService) {
        this.resultsService = resultsService;
        this.studentService = studentService;
    }

    //get class results of an academic year
    @GetMapping("/classResults")
    public ResponseEntity<List<Results>> getClassResults(
            @RequestParam("classCode") String classCode,
            @RequestParam("academicYear") String acyear

    ){
        try{
            List<Results> classResults = resultsService.getClassResult(classCode,acyear);
            return new ResponseEntity<>(classResults,HttpStatus.OK);
        }
        catch(InvalidValueException | BlankValueException | NotFoundException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
        catch(Exception e){
            return new ResponseEntity("Action failed!",HttpStatus.EXPECTATION_FAILED);
        }
    }


    //get class results of particular test/exam
    @GetMapping("/classTest")
    public ResponseEntity<List<Results>> getClassTestResults(
            @RequestParam("className") String className,
            @RequestParam("academicYear") String acyear,
             @RequestParam("scheduleName") String scName
    ){
        try{
            List<Results> classTestResults = resultsService.getClassTestResults(className,acyear,scName);
            return new ResponseEntity<>(classTestResults,HttpStatus.OK);
        }
        catch(InvalidValueException | BlankValueException | NotFoundException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
        catch(Exception e){
            return new ResponseEntity("Action Failed",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping
    public ResponseEntity<String> addResults(@RequestBody @Valid Results result){
        try{
            resultsService.addNewResult(result);
            return new ResponseEntity<>("Successful",HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/{resultCode}")
    public ResponseEntity<String> updateResult(@PathVariable String resultCode, @RequestBody Results result){
        try{
            resultsService.updateResult(resultCode,result);
            return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity("Update failed! Please try again",HttpStatus.EXPECTATION_FAILED);
        }
    }

    //delete result
    @DeleteMapping("/delete/{resultCode}")
    public ResponseEntity<String> deleleResults(@PathVariable String resultCode){
        try{
            resultsService.deleteResult(resultCode);
            return new ResponseEntity<>("Deleted Sucessfully",HttpStatus.OK);
        }
        catch(NotFoundException e){
            return new ResponseEntity<>("Result does not Exists",HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity<>("Deletion Failed",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/avgInternal")
    public ResponseEntity<Float> getAvgInternalForSubject(
            @RequestParam("studentId") String studentId,
            @RequestParam("acYear") String acYear,
            @RequestParam("subjectCode") String subjectCode
    ){
        try {
            Student student = studentService.getStudentInfo(studentId).orElse(null);
            if(student==null){
                throw new NotFoundException("Student not found");
            }
            Float avgInternals = resultsService.getAverageForSubject(student,acYear,subjectCode);
            return new ResponseEntity<>(avgInternals,HttpStatus.OK);
        } catch (NotFoundException | InvalidValueException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity("Action failed!",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/percentage")
    public ResponseEntity<Double> getResultPercentageOfStudent(
            @RequestParam("studentId") String studentId,
            @RequestParam("acYear") String acYear
    ){
        try{
            double percentage = resultsService.getResultPercentage(studentId,acYear);
            return new ResponseEntity<>(percentage,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/student")
    public ResponseEntity<List<Results>> getStudentResults(
            @RequestParam("studentId") String studentId,
            @RequestParam("acYear") String acYear
            ){
        try{
            Student student = studentService.getStudentInfo(studentId).orElse(null);
            if(student==null){
                throw new NotFoundException("Student not found");
            }
            List<Results> resultsList = resultsService.getStudentResult(student,acYear);
            return new ResponseEntity<>(resultsList,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }
}

