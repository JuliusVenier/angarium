package com.angarium.repository;

import com.angarium.entity.FileMetaDataEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class FileMetaDataRepository implements PanacheRepository<FileMetaDataEntity> {

    public FileMetaDataEntity findFileMetaDataByUUID(UUID uuid) {
        return find("id", uuid).firstResult();
    }

}
