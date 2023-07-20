package com.target.targetreadyresultsservice.Exception;

public class BlankValueException extends RuntimeException{
    public BlankValueException(){

    }
    public BlankValueException(String message){
        super(message);
    }
    public BlankValueException(Throwable cause){
        super(cause);
    }
}
