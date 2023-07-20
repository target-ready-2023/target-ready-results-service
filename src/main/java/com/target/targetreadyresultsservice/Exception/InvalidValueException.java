package com.target.targetreadyresultsservice.Exception;

public class InvalidValueException extends RuntimeException{
    public InvalidValueException(){

    }
    public InvalidValueException(String message){
        super(message);
    }
    public InvalidValueException(Throwable cause){
        super(cause);
    }
}
