package com.angarium.web;

import com.angarium.model.FileIdModel;
import com.angarium.model.NewFileMetaDataModel;
import com.angarium.service.FileService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

import java.io.File;
import java.io.IOException;

@Path("/upload")
@JBossLog
@RequiredArgsConstructor
public class UploadResource {

    private final FileService fileService;

    @PUT
    @Path("/{name}")
    @RunOnVirtualThread
    @RolesAllowed("user")
    public FileIdModel upload(@Valid @BeanParam NewFileMetaDataModel newFileMetaDataModel, File file) throws IOException {
        return fileService.upload(newFileMetaDataModel, file);

    }
}
