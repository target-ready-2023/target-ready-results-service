package com.target.targetreadyresultsservice.Exception;

public class NullValueException extends RuntimeException{
    public NullValueException(){

    }
    public NullValueException(String message){
        super(message);
    }
    public NullValueException(Throwable cause){
        super(cause);
    }
}
