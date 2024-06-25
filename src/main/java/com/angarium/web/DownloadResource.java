package com.angarium.web;

import com.angarium.model.DownloadModel;
import com.angarium.service.FileService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.io.IOException;

@Path("/download")
@JBossLog
@RequiredArgsConstructor
public class DownloadResource {
    private final FileService fileService;

    /**
     * GET-Ressource, um eine Datei herunterzuladen.
     *
     * @param id Die ID der herunterzuladenden Datei.
     * @return Eine Response mit der herunterzuladenden Datei.
     * @throws IOException Wenn beim Herunterladen der Datei ein Fehler auftritt.
     */
    @GET
    @Path("/{id}")
    @RunOnVirtualThread
    @Operation(summary = "Lädt eine Datei herunter",
            description = "Diese Methode lädt eine Datei basierend auf der angegebenen ID herunter.")
    public Response download(String id
    ) throws IOException {
        DownloadModel downloadModel = fileService.download(id);
        Response.ResponseBuilder response = Response.ok(downloadModel.getFile());
        response.header("Content-Disposition", "attachment;filename=" + downloadModel.getFileMetaDataModel().getName());
        return response.build();
    }
}
