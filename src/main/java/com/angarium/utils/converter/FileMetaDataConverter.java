package com.angarium.utils.converter;

import com.angarium.entity.FileMetaDataEntity;
import com.angarium.entity.UserEntity;
import com.angarium.model.FileMetaDataModel;
import com.angarium.model.NewFileMetaDataModel;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDate;

@ApplicationScoped
@RequiredArgsConstructor
public class FileMetaDataConverter {
    private final UserConverter userConverter;

    @ConfigProperty(name = "angarium.config.max-days")
    int maxDays;

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
    public FileMetaDataEntity toFileMetaDataEntity(NewFileMetaDataModel newFileMetaDataModel, UserEntity userEntity) {

        return FileMetaDataEntity.builder()
                .name(newFileMetaDataModel.getName())
                .maxDownloads(newFileMetaDataModel.getMaxDownloads())
                .creationDate(LocalDate.now())
                .deletionDate(LocalDate.now().plusDays(calcMaxDays(newFileMetaDataModel)))
                .sha256(newFileMetaDataModel.getSha256())
                .encrypted(newFileMetaDataModel.getEncrypted())
                .userEntity(userEntity)
                .build();
    }

    private int calcMaxDays(NewFileMetaDataModel newFileMetaDataModel){
        if (newFileMetaDataModel.getMaxDays() == null){
            return maxDays;
        }

        return newFileMetaDataModel.getMaxDays();
    }

    /**
     * Konvertiert ein 'FileMetaDataEntity'-Objekt in ein 'FileMetaDataModel'-Objekt.
     *
     * @param fileMetaDataEntity Das zu konvertierende 'FileMetaDataEntity'-Objekt
     *
     * @return Ein neues 'FileMetaDataModel'-Objekt mit den konvertierten Daten.
     */
    public FileMetaDataModel toFileMetaDataModel(FileMetaDataEntity fileMetaDataEntity) {
        return new FileMetaDataModel(
                fileMetaDataEntity.getId(),
                fileMetaDataEntity.getName(),
                fileMetaDataEntity.getMaxDownloads(),
                fileMetaDataEntity.getCurrentDownloads(),
                fileMetaDataEntity.getCreationDate(),
                fileMetaDataEntity.getDeletionDate(),
                fileMetaDataEntity.getSha256(),
                fileMetaDataEntity.isEncrypted(),
                userConverter.toUserModel(fileMetaDataEntity.getUserEntity())
        );
    }
}
