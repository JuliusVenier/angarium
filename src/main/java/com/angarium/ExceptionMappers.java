package com.angarium;

import com.angarium.service.exception.FileDownloadLimitsReachedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.stream.Collectors;

/**
 * Diese Klasse bietet Methoden zum Abbilden verschiedener Ausnahmen auf HTTP-Antworten.
 * Jede Methode fängt eine spezifische Ausnahme ab und gibt eine entsprechende HTTP-Antwort zurück.
 */
@Slf4j
public class ExceptionMappers {

    /**
     * Mapper für allgemeine Ausnahmen, die nicht spezifisch behandelt werden.
     * Gibt eine HTTP-Antwort mit dem Status 500 (Internal Server Error) und einer generischen Fehlermeldung zurück.
     *
     * @param e die abgefangene Ausnahme
     * @return eine RestResponse mit Status 500 und einer Fehlermeldung
     */
    @ServerExceptionMapper
    public RestResponse<String> mapException(Exception e) {
        log.error(e.getMessage(), e);
        return RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR, "An unexpected error has occured.");
    }

    /**
     * Mapper für IllegalArgumentExceptions.
     * Gibt eine HTTP-Antwort mit dem Status 400 (Bad Request) und der Fehlermeldung der Ausnahme zurück.
     *
     * @param e die abgefangene IllegalArgumentException
     * @return eine RestResponse mit Status 400 und der Fehlermeldung der Ausnahme
     */
    @ServerExceptionMapper
    public RestResponse<String> mapIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, e.getMessage());
    }

    /**
     * Mapper für ConstraintViolationExceptions.
     * Gibt eine HTTP-Antwort mit dem Status 400 (Bad Request) und einer zusammengeführten Fehlermeldung aller Constraint-Verletzungen zurück.
     *
     * @param e die abgefangene ConstraintViolationException
     * @return eine RestResponse mit Status 400 und einer zusammengeführten Fehlermeldung
     */
    @ServerExceptionMapper
    public RestResponse<String> mapConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage());
        return RestResponse.status(RestResponse.Status.BAD_REQUEST,
                e.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", ")));
    }

    /**
     * Mapper für FileDownloadLimitsReachedExceptions.
     * Gibt eine HTTP-Antwort mit dem Status 400 (Bad Request) und der Fehlermeldung der Ausnahme zurück.
     *
     * @param e die abgefangene FileDownloadLimitsReachedException
     * @return eine RestResponse mit Status 400 und der Fehlermeldung der Ausnahme
     */
    @ServerExceptionMapper
    public RestResponse<String> mapFileDownloadLimitsReachedException(FileDownloadLimitsReachedException e) {
        log.error(e.getMessage());
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, e.getMessage());
    }
}
