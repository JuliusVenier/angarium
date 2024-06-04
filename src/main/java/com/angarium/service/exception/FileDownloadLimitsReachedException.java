package com.angarium.service.exception;

public class FileDownloadLimitsReachedException extends RuntimeException{
    public FileDownloadLimitsReachedException(String msg) {
        super(msg);
    }
}
