package com.angarium.utils.converter;

import com.angarium.entity.FileMetaDataEntity;
import com.angarium.entity.UserEntity;
import com.angarium.model.FileMetaDataModel;
import com.angarium.model.NewFileMetaDataModel;

import java.time.LocalDate;

public class FileMetaDataConverter {

    /**
     * Konvertiert ein 'NewFileMetaDataModel'-Objekt in ein 'FileMetaDataEntity'-Objekt.
     * <p>
     * Dabei werden die folgenden Eigenschaften übertragen:
     *  * name: Der Name der Datei
     *  * maxDownloads: Die maximale Anzahl zulässiger Downloads
     * <p>
     * Das Erstellungsdatum ('creationDate') wird auf das aktuelle Datum gesetzt.
     * Das Löschdatum ('deletionDate') wird durch Addition der maximalen Speichertage ('maxDays') zum aktuellen Datum berechnet.
     *
     * @param newFileMetaDataModel Das zu konvertierende 'NewFileMetaDataModel'-Objekt.
     * @param userEntity Der User der die Datei hochgeladen hat
     *
     * @return Ein neues 'FileMetaDataEntity'-Objekt mit den konvertierten Daten.
     */
    public static FileMetaDataEntity toFileMetaDataEntity(NewFileMetaDataModel newFileMetaDataModel, UserEntity userEntity) {

        return FileMetaDataEntity.builder()
                .name(newFileMetaDataModel.getName())
                .maxDownloads(newFileMetaDataModel.getMaxDownloads())
                .creationDate(LocalDate.now())
                .deletionDate(LocalDate.now().plusDays(newFileMetaDataModel.getMaxDays()))
                .sha256(newFileMetaDataModel.getSha256())
                .userEntity(userEntity)
                .build();
    }

    /**
     * Konvertiert ein 'FileMetaDataEntity'-Objekt in ein 'FileMetaDataModel'-Objekt.
     *
     * @param fileMetaDataEntity Das zu konvertierende 'FileMetaDataEntity'-Objekt
     *
     * @return Ein neues 'FileMetaDataModel'-Objekt mit den konvertierten Daten.
     */
    public static FileMetaDataModel toFileMetaDataModel(FileMetaDataEntity fileMetaDataEntity) {
        return new FileMetaDataModel(
                fileMetaDataEntity.getId(),
                fileMetaDataEntity.getName(),
                fileMetaDataEntity.getMaxDownloads(),
                fileMetaDataEntity.getCurrentDownloads(),
                fileMetaDataEntity.getCreationDate(),
                fileMetaDataEntity.getDeletionDate(),
                fileMetaDataEntity.getSha256(),
                UserConverter.toUserModel(fileMetaDataEntity.getUserEntity())
        );
    }
}
