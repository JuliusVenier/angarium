package com.angarium.web;

import com.angarium.model.FileMetaDataModel;
import com.angarium.service.FileService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Diese Klasse stellt verschiedene Endpunkte zur Verwaltung von Dateimetadaten bereit.
 */
@Path("/meta-data")
@RequiredArgsConstructor
public class MetaDataResource {
    private final FileService fileService;

    /**
     * GET-Ressource, die die Metadaten einer Datei anhand ihrer ID zurückgibt.
     *
     * @param fileId Die ID der Datei, deren Metadaten abgerufen werden sollen.
     * @return Die Metadaten der angegebenen Datei.
     */
    @GET
    @Path("/{fileId}")
    @RunOnVirtualThread
    @Operation(summary = "Gibt die Metadaten einer Datei zurück",
            description = "Diese Methode gibt die Metadaten einer Datei basierend auf der angegebenen ID zurück.")
    public FileMetaDataModel getMetaData(String fileId) {
        return fileService.getFileMetaData(fileId);
    }

    /**
     * GET-Ressource, die eine Liste der Dateien des aktuellen Benutzers zurückgibt.
     *
     * @return Eine Liste von FileMetaDataModel-Objekten der Dateien des aktuellen Benutzers.
     */
    @GET
    @Path("/me/files")
    @RunOnVirtualThread
    @RolesAllowed("user")
    @Operation(summary = "Gibt die Dateien des aktuellen Benutzers zurück",
            description = "Diese Methode gibt eine Liste der Dateien des aktuell angemeldeten Benutzers zurück.")
    public List<FileMetaDataModel> getMyFiles() {
        return fileService.getMyFiles();
    }

    /**
     * DELETE-Ressource, um eine Datei des aktuellen Benutzers zu löschen.
     *
     * @param fileId Die ID der zu löschenden Datei.
     * @throws IOException Wenn beim Löschen der Datei ein Fehler auftritt.
     */
    @DELETE
    @Path("/me/{fileId}")
    @RunOnVirtualThread
    @RolesAllowed("user")
    @Operation(summary = "Löscht eine Datei des aktuellen Benutzers",
            description = "Diese Methode löscht eine Datei des aktuell angemeldeten Benutzers basierend auf der angegebenen ID.")
    public void deleteFiles(String fileId) throws IOException {
        fileService.deleteMyFiles(UUID.fromString(fileId));
    }
}
