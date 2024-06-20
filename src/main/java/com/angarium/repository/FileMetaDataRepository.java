package com.angarium.repository;

import com.angarium.entity.FileMetaDataEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Repository-Klasse zur Verwaltung von Datei-Metadaten in der Datenbank.
 * Diese Klasse bietet Methoden zum Suchen, Löschen und Auflisten von Datei-Metadaten.
 */
@ApplicationScoped
@JBossLog
public class FileMetaDataRepository implements PanacheRepository<FileMetaDataEntity> {

    /**
     * Sucht eine Datei-Metadaten-Entität anhand ihrer UUID.
     *
     * @param uuid die UUID der Datei-Metadaten-Entität.
     * @return die gefundene Datei-Metadaten-Entität oder null, wenn keine Entität gefunden wurde.
     */
    public FileMetaDataEntity findFileMetaDataByUUID(UUID uuid) {
        return find("id", uuid).firstResult();
    }

    /**
     * Löscht eine Datei-Metadaten-Entität anhand ihrer UUID.
     *
     * @param uuid die UUID der zu löschenden Datei-Metadaten-Entität.
     */
    public void deleteFileMetaDataByUUID(UUID uuid) {
        delete("id", uuid);
    }

    /**
     * Findet alle Dateien, die ihre maximalen Download-Limits erreicht haben.
     *
     * @return eine Liste von Datei-Metadaten-Entitäten, die ihre Download-Limits erreicht haben.
     */
    public List<FileMetaDataEntity> findFilesThatHaveReachedTheirLimits(){
        return list("select f from FileMetaDataEntity f where f.max_downloads = f.current_downloads and f.deletion_date = ?1", LocalDate.now());
    }

    /**
     * Löscht alle Dateien, die ihre maximalen Download-Limits erreicht haben.
     */
    public void deleteFilesThatHaveReachedTheirLimits(){
        delete("delete FileMetaDataEntity where max_downloads = current_downloads and deletion_date = ?1", LocalDate.now());
    }

    /**
     * Findet alle Dateien, die zu einem bestimmten Benutzer gehören.
     *
     * @param username der Benutzername, zu dem die Dateien gesucht werden.
     * @return eine Liste von Datei-Metadaten-Entitäten, die zu dem angegebenen Benutzer gehören.
     */
    public List<FileMetaDataEntity> findFilesByUsername(String username) {
        return list("select f from FileMetaDataEntity f join f.userEntity u where u.username = ?1", username);
    }

}
