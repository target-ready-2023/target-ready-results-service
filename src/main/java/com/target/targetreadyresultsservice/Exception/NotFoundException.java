package com.target.targetreadyresultsservice.Exception;

public class NotFoundException  extends RuntimeException{
    public NotFoundException(){

    }
    public NotFoundException(String message){
        super(message);
    }
    public NotFoundException(Throwable cause){
        super(cause);
    }
}
