package com.angarium.service.exception;

public class FileLimitsReachedException extends RuntimeException{
    public FileLimitsReachedException(String msg) {
        super(msg);
    }
}
