package com.angarium.web;

import com.angarium.model.DownloadModel;
import com.angarium.service.FileService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

import java.io.IOException;

@Path("/download")
@JBossLog
@RequiredArgsConstructor
public class DownloadResource {
    private final FileService fileService;

    @GET
    @Path("/{id}")
    @RunOnVirtualThread
    public Response download(String id) throws IOException {
        DownloadModel downloadModel = fileService.download(id);
        Response.ResponseBuilder response = Response.ok(downloadModel.getFile());
        response.header("Content-Disposition", "attachment;filename=" + downloadModel.getFileMetaDataModel().getName());
        return response.build();
    }
}
