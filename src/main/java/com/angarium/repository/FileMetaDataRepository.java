package com.angarium.repository;

import com.angarium.entity.FileMetaDataEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@JBossLog
public class FileMetaDataRepository implements PanacheRepository<FileMetaDataEntity> {

    public FileMetaDataEntity findFileMetaDataByUUID(UUID uuid) {
        return find("id", uuid).firstResult();
    }

    public void deleteFileMetaDataByUUID(UUID uuid) {
        delete("id", uuid);
    }

    public List<FileMetaDataEntity> findFilesThatHaveReachedTheirLimits(){
        return list("from file-meta-data where max-downloads = current-downloads and deletion-date = ?1", LocalDate.now());
    }

    public void deleteFilesThatHaveReachedTheirLimits(){
        delete("delete file-meta-data where max-downloads = current-downloads and deletion-date = ?1", LocalDate.now());
    }

}
