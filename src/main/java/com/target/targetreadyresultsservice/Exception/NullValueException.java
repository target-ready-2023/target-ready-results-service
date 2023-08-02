package com.target.targetreadyresultsservice.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullValueException extends RuntimeException{

    private static final Logger log = LoggerFactory.getLogger(NullValueException.class);
    public String NullValueException(){
        log.info("Null value exception occurred");
        return "Null value provided";
    }
    public NullValueException(String message){
        super(message);
    }
    public NullValueException(Throwable cause){
        super(cause);
    }
}
