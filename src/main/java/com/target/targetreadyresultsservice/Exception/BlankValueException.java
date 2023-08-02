package com.target.targetreadyresultsservice.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlankValueException extends RuntimeException{

    private static final Logger log = LoggerFactory.getLogger(BlankValueException.class);

    public String BlankValueException(){
        log.info("BlankValueException occurred");
        return "Blank value provided";
    }
    public BlankValueException(String message){

        super(message);
    }
    public BlankValueException(Throwable cause){
        super(cause);
    }
}
