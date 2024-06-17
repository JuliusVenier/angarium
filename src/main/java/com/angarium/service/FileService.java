package com.angarium.service;

import com.angarium.entity.FileMetaDataEntity;
import com.angarium.entity.UserEntity;
import com.angarium.model.DownloadModel;
import com.angarium.model.FileIdModel;
import com.angarium.model.FileMetaDataModel;
import com.angarium.model.NewFileMetaDataModel;
import com.angarium.repository.FileMetaDataRepository;
import com.angarium.repository.UserRepository;
import com.angarium.service.exception.FileDownloadLimitsReachedException;
import com.angarium.utils.converter.FileMetaDataConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Die FileService-Klasse bietet Funktionen zum Hochladen, Herunterladen und Löschen von Dateien.
 * Sie verwaltet außerdem die Datei-Metadaten und überwacht die Download-Limits.
 */
@ApplicationScoped
@RequiredArgsConstructor
@JBossLog
public class FileService {
    private final FileMetaDataRepository fileMetaDataRepository;
    private final UserRepository userRepository;
    private final FileMetaDataConverter fileMetaDataConverter;

    @ConfigProperty(name = "anagarium.config.dir")
    String fileDir;

    @Context
    SecurityContext securityContext;

    /**
     * Lädt eine neue Datei hoch und speichert die Metadaten.
     *
     * @param newFileMetaDataModel Die Metadaten der hochzuladenden Datei.
     * @param file Die Datei, die hochgeladen werden soll.
     * @return Ein Modell, das die ID der hochgeladenen Datei enthält.
     * @throws IOException Wenn ein Fehler beim Verschieben der Datei auftritt.
     */
    @Transactional
    public FileIdModel upload(NewFileMetaDataModel newFileMetaDataModel, File file) throws IOException {
        UserEntity userEntity =  userRepository.findUserByUsername(securityContext.getUserPrincipal().getName());
        FileMetaDataEntity fileMetaDataEntity = fileMetaDataConverter.toFileMetaDataEntity(newFileMetaDataModel, userEntity);
        fileMetaDataRepository.persist(fileMetaDataEntity);

        moveFile(file, fileMetaDataEntity.getId().toString());
        return new FileIdModel(fileMetaDataEntity.getId().toString());
    }

    /**
     * Verschiebt eine Datei in das Zielverzeichnis.
     *
     * @param file Die Datei, die verschoben werden soll.
     * @param fileId Die ID der Datei.
     * @throws IOException Wenn ein Fehler beim Verschieben der Datei auftritt.
     */
    private void moveFile(File file, String fileId) throws IOException {
        Path target = Paths.get(fileDir, fileId);
        Files.createDirectories(target.getParent());
        Files.move(file.toPath(), target);
    }

    /**
     * Lädt eine Datei herunter und aktualisiert die Download-Zähler.
     *
     * @param fileId Die ID der herunterzuladenden Datei.
     * @return Ein Modell, das die Datei und ihre Metadaten enthält.
     * @throws IOException Wenn ein Fehler beim Herunterladen der Datei auftritt.
     */
    @Transactional(dontRollbackOn = FileDownloadLimitsReachedException.class)
    public DownloadModel download(String fileId) throws IOException {
        FileMetaDataEntity fileMetaDataEntity = findFileMetaData(fileId);
        int currentDownloads = fileMetaDataEntity.getCurrentDownloads();

        try {
            checkFileDownloadLimits(fileMetaDataEntity);
        } catch (FileDownloadLimitsReachedException ex) {
            deleteFile(fileMetaDataEntity.getId());
            throw ex;
        }

        fileMetaDataEntity.setCurrentDownloads(++currentDownloads);
        fileMetaDataRepository.persist(fileMetaDataEntity);

        FileMetaDataModel fileMetaDataModel = fileMetaDataConverter.toFileMetaDataModel(fileMetaDataEntity);

        return new DownloadModel(new File(Paths.get(fileDir, fileId).toUri()), fileMetaDataModel);
    }

    /**
     * Überprüft die Download-Limits einer Datei.
     *
     * @param fileMetaDataEntity Die Metadaten der Datei.
     */
    private void checkFileDownloadLimits(FileMetaDataEntity fileMetaDataEntity) {
        if(fileMetaDataEntity.getCurrentDownloads() >= fileMetaDataEntity.getMaxDownloads()){
            throw new FileDownloadLimitsReachedException("Maximum number of downloads has been reached");
        }

        if(LocalDate.now().equals(fileMetaDataEntity.getDeletionDate())) {
            throw new FileDownloadLimitsReachedException("File has reached its deletion date and cannot be downloaded");
        }
    }

    /**
     * Löscht Dateien, die ihr Download-Limit erreicht haben.
     *
     * @throws IOException Wenn ein Fehler beim Löschen der Datei auftritt.
     */
    @Transactional
    public void deleteFilesThatHaveReachedTheirLimit() throws IOException {
        List<FileMetaDataEntity> entities = fileMetaDataRepository.findFilesThatHaveReachedTheirLimits();
        fileMetaDataRepository.deleteFilesThatHaveReachedTheirLimits();

        for(FileMetaDataEntity entity: entities){
            deleteFile(entity.getId());
        }
    }

    /**
     * Löscht eine Datei.
     *
     * @param fileId Die ID der Datei, die gelöscht werden soll.
     * @throws IOException Wenn ein Fehler beim Löschen der Datei auftritt.
     */
    public void deleteFile(UUID fileId) throws IOException {
        Files.deleteIfExists(Paths.get(fileDir, fileId.toString()));
        fileMetaDataRepository.deleteFileMetaDataByUUID(fileId);
    }

    /**
     * Findet die Metadaten einer Datei anhand ihrer ID.
     *
     * @param fileId Die ID der Datei.
     * @return Die Metadaten der Datei.
     */
    private FileMetaDataEntity findFileMetaData(String fileId) {
        return fileMetaDataRepository.findFileMetaDataByUUID(UUID.fromString(fileId));
    }

    public FileMetaDataModel getFileMetaData(String fileId) {
        return fileMetaDataConverter.toFileMetaDataModel(findFileMetaData(fileId));
    }


    /**
     * Ruft die Dateien des aktuellen Benutzers ab.
     *
     * @return Eine Liste von Modellen, die die Metadaten der Dateien des Benutzers enthalten.
     */
    public List<FileMetaDataModel> getMyFiles() {
        return fileMetaDataRepository.findFilesByUsername(securityContext.getUserPrincipal().getName()).stream()
                .map(fileMetaDataConverter::toFileMetaDataModel)
                .toList();
    }
}
