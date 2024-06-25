package com.angarium.service.exception;

public class FileDownloadLimitsReachedException extends RuntimeException{

    /**
     * Diese Ausnahme wird ausgelöst, wenn das Download-Limit oder das Tage-Limit einer Datei erreicht ist.
     */
    public FileDownloadLimitsReachedException(String msg) {
        super(msg);
    }
}
