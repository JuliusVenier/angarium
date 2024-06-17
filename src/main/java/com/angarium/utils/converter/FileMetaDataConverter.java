package com.angarium.utils.converter;

import com.angarium.entity.FileMetaDataEntity;
import com.angarium.entity.UserEntity;
import com.angarium.model.FileMetaDataModel;
import com.angarium.model.NewFileMetaDataModel;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDate;

@ApplicationScoped
@RequiredArgsConstructor
public class FileMetaDataConverter {
    private final UserConverter userConverter;

    @ConfigProperty(name = "angarium.config.default-days")
    int defaultDays;

    @ConfigProperty(name = "angarium.config.default-downloads")
    int defaultDownloads;

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
                .maxDownloads(calcMaxDownloads(newFileMetaDataModel))
                .creationDate(LocalDate.now())
                .deletionDate(LocalDate.now().plusDays(calcMaxDays(newFileMetaDataModel)))
                .sha256(newFileMetaDataModel.getSha256())
                .encrypted(newFileMetaDataModel.getEncrypted() != null && newFileMetaDataModel.getEncrypted())
                .userEntity(userEntity)
                .build();
    }

    /**
     * Berechnet die maximale Anzahl von Downloads für die Datei.
     *
     * @param newFileMetaDataModel Das Modell mit den neuen Dateimetadaten.
     * @return Die maximale Anzahl von Downloads.
     */
    private int calcMaxDays(NewFileMetaDataModel newFileMetaDataModel){
        if (newFileMetaDataModel.getMaxDays() == null){
            return defaultDays;
        }

        return newFileMetaDataModel.getMaxDays();
    }

    /**
     * Berechnet die maximale Anzahl von Downloads für die Datei.
     *
     * @param newFileMetaDataModel Das Modell mit den neuen Dateimetadaten.
     * @return Die maximale Anzahl von Downloads.
     */
    private int calcMaxDownloads(NewFileMetaDataModel newFileMetaDataModel){
        if (newFileMetaDataModel.getMaxDays() == null){
            return defaultDownloads;
        }

        return newFileMetaDataModel.getMaxDownloads();
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
