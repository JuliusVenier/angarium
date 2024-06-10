package com.angarium.web;

import com.angarium.entity.FileMetaDataEntity;
import com.angarium.model.FileMetaDataModel;
import com.angarium.service.FileService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Path("/api/meta-data")
@RequiredArgsConstructor
public class MetaDataResource {
    private final FileService fileService;

    @GET
    @Path("/{fileId}")
    @RunOnVirtualThread
    public FileMetaDataModel getMetaData(String fileId) {
        return fileService.getFileMetaData(fileId);
    }

    @GET
    @Path("/me")
    @RunOnVirtualThread
    public List<FileMetaDataModel> getMyFiles() {
        return fileService.getMyFiles();
    }
}
