package com.angarium.web;

import com.angarium.model.DownloadModel;
import com.angarium.service.FileService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

import java.io.File;

@Path("/api/download")
@JBossLog
@RequiredArgsConstructor
public class DownloadResource {
    private final FileService fileService;

    @PUT
    @Path("/{id}")
    @RunOnVirtualThread
    public Response download(String id) {
        DownloadModel downloadModel = fileService.download(id);
        Response.ResponseBuilder response = Response.ok((Object) downloadModel.getFile());
        response.header("Content-Disposition", "attachment;filename=" + downloadModel.getFileMetaDataModel().getName());
        return response.build();
    }
}
