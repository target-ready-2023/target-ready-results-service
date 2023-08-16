package com.target.targetreadyresultsservice.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotFoundException extends RuntimeException{

    private static final Logger log = LoggerFactory.getLogger(NotFoundException.class);
    public String NotFoundException(){
        log.info("Not found exception occurred");
        return "Not found here";
    }
    public NotFoundException(String message){
        super(message);
    }
    public NotFoundException(Throwable cause){
        super(cause);
    }
}