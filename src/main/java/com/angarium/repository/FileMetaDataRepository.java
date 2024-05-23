package com.angarium.repository;

import com.angarium.entity.FileMetaDataEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileMetaDataRepository implements PanacheRepository<FileMetaDataEntity> {
}
