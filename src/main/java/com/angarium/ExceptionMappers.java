package com.angarium;

import com.angarium.service.exception.FileDownloadLimitsReachedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.stream.Collectors;

public class ExceptionMappers {
    @ServerExceptionMapper
    public RestResponse<String> mapException(Exception e) {
        return RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR, "An unexpected error has occured.");
    }

    @ServerExceptionMapper
    public RestResponse<String> mapIllegalArgumentException(IllegalArgumentException e) {
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, e.getMessage());
    }

    @ServerExceptionMapper
    public RestResponse<String> mapConstraintViolationException(ConstraintViolationException e) {
        return RestResponse.status(RestResponse.Status.BAD_REQUEST,
                e.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", ")));
    }

    @ServerExceptionMapper
    public RestResponse<String> mapFileDownloadLimitsReachedException(FileDownloadLimitsReachedException e) {
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, e.getMessage());
    }
}
