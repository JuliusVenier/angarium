package com.angarium.web;

import com.angarium.model.NewFileMetaDataModel;
import com.angarium.service.FileService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

import java.io.File;
import java.io.IOException;

@Path("/api/upload")
@JBossLog
@RequiredArgsConstructor
public class UploadResource {

    private final FileService fileService;

    @PUT
    @Path("/{name}")
    @RunOnVirtualThread
    public void upload(@BeanParam NewFileMetaDataModel newFileMetaDataModel, File file) throws IOException {
        fileService.upload(newFileMetaDataModel, file);

    }

    @GET
    @Path("/{id}")
    @RunOnVirtualThread
    public File download(){
        return null;
    }
}
