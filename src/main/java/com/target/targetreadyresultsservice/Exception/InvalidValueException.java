package com.target.targetreadyresultsservice.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidValueException extends RuntimeException{

    private static final Logger log = LoggerFactory.getLogger(InvalidValueException.class);

    public String InvalidValueException(){
        log.info("Invalid value exception occurred");
        return "Invalid value provided";
    }
    public InvalidValueException(String message){
        super(message);
    }
    public InvalidValueException(Throwable cause){
        super(cause);
    }
}
