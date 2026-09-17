package com.example.userservice.Exceptions;

public class WrongPasswordException extends  Exception{
    public WrongPasswordException(String message){
        super(message);
    }
}
