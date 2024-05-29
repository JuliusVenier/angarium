package com.angarium.web;

import com.angarium.service.FileService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

import java.io.File;

@Path("/api/upload")
@JBossLog
@RequiredArgsConstructor
public class DownloadResource {
    private final FileService fileService;

    @PUT
    @Path("/{id}")
    @RunOnVirtualThread
    public Response download(String id) {

        Object nf = new Object();

        Response.ResponseBuilder response = Response.ok((Object) nf);
        response.header("Content-Disposition", "attachment;filename=" + nf);
        return response.build();
    }
}
