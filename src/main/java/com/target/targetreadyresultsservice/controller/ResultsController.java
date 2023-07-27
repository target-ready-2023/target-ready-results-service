package com.target.targetreadyresultsservice.controller;

import com.target.targetreadyresultsservice.Exception.NotFoundException;
import com.target.targetreadyresultsservice.model.Results;
import com.target.targetreadyresultsservice.service.ResultsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("results/v1")
public class ResultsController {
    private final ResultsService resultsService;

    public ResultsController(ResultsService resultsService) {
        this.resultsService = resultsService;
    }

    @PostMapping
    public ResponseEntity<String> addResults(@RequestBody @Valid Results result){
        try{
            resultsService.addNewResult(result);
            return new ResponseEntity<>("Success",HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

        catch (Exception e){
            return new ResponseEntity<>("Error",HttpStatus.EXPECTATION_FAILED);
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
}
