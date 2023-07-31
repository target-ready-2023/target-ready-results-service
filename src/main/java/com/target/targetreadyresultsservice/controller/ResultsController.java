package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.Exception.BlankValueException;
import com.target.targetreadyresultsservice.Exception.InvalidValueException;
import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.model.Results;
import com.target.targetreadyresultsservice.service.ResultsService;
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

    public ResultsController(ResultsService resultsService) {
        this.resultsService = resultsService;
    }

    //get class results of an academic year
    @GetMapping
    public ResponseEntity<List<Results>> getClassResults(
            @RequestParam("classCode") String classCode,
            @RequestParam("academicYear") String acyear

    ){
        try{
            List<Results> classResults = resultsService.getClassresult(classCode,acyear);
            return new ResponseEntity<List<Results>>(classResults,HttpStatus.OK);
        }
        catch(NotFoundException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch(InvalidValueException | BlankValueException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
        catch(Exception e){
            return new ResponseEntity("Action Failed",HttpStatus.EXPECTATION_FAILED);
        }
    }


    //get class results of particular test/exam
    @GetMapping
    public ResponseEntity<List<Results>> getClassTestResults(
            @RequestParam("className") String className,
            @RequestParam("academicYear") String acyear,
             @RequestParam("scheudleName") String scName
    ){
        try{
            List<Results> classTestResults = resultsService.getClassTestResults(className,acyear,scName);
            return new ResponseEntity<List<Results>>(classTestResults,HttpStatus.OK);
        }
        catch(NotFoundException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch(InvalidValueException | BlankValueException e){
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
}

