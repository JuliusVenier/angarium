package com.angarium.service.exception;

/**
 * Diese Ausnahme wird ausgelöst, wenn das Download-Limit oder das Tage-Limit einer Datei erreicht ist.
 */
public class FileDownloadLimitsReachedException extends RuntimeException{
    public FileDownloadLimitsReachedException(String msg) {
        super(msg);
    }
}
