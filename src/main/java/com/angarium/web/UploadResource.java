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
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.io.File;
import java.io.IOException;

/**
 * Diese Klasse stellt einen Endpunkt zum Hochladen von Dateien bereit.
 */
@Path("/upload")
@JBossLog
@RequiredArgsConstructor
public class UploadResource {

    private final FileService fileService;

    /**
     * PUT-Ressource, um eine Datei hochzuladen.
     *
     * @param newFileMetaDataModel Die Metadaten des neuen hochzuladenden Files.
     * @param file Die hochzuladende Datei.
     * @return Das Modell, das die ID der hochgeladenen Datei enthält.
     * @throws IOException Wenn beim Hochladen der Datei ein Fehler auftritt.
     */
    @PUT
    @Path("/{name}")
    @RunOnVirtualThread
    @RolesAllowed("user")
    @Operation(summary = "Lädt eine Datei hoch",
            description = "Diese Methode lädt eine Datei basierend auf den angegebenen Metadaten hoch.")
    public FileIdModel upload(
            @Valid @BeanParam NewFileMetaDataModel newFileMetaDataModel,
            File file
    ) throws IOException {
        return fileService.upload(newFileMetaDataModel, file);

    }
}
