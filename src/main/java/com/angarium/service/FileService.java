package com.angarium.service;

import com.angarium.entity.FileMetaDataEntity;
import com.angarium.entity.UserEntity;
import com.angarium.model.DownloadModel;
import com.angarium.model.FileIdModel;
import com.angarium.model.FileMetaDataModel;
import com.angarium.model.NewFileMetaDataModel;
import com.angarium.repository.FileMetaDataRepository;
import com.angarium.repository.UserRepository;
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
import java.util.UUID;

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

    @Transactional
    public FileIdModel upload(NewFileMetaDataModel newFileMetaDataModel, File file) throws IOException {
        UserEntity userEntity =  userRepository.findUserByUsername("default"); //securityContext.getUserPrincipal().getName());
        FileMetaDataEntity fileMetaDataEntity = fileMetaDataConverter.toFileMetaDataEntity(newFileMetaDataModel, userEntity);
        fileMetaDataRepository.persist(fileMetaDataEntity);

        moveFile(file, fileMetaDataEntity.getId().toString());
        return new FileIdModel(fileMetaDataEntity.getId().toString());
    }

    private void moveFile(File file, String fileId) throws IOException {
        Path target = Paths.get(fileDir, fileId);
        Files.createDirectories(target.getParent());
        Files.move(file.toPath(), target);
    }

    public DownloadModel download(String fileId) {
        FileMetaDataModel fileMetaDataModel = fileMetaDataConverter.toFileMetaDataModel(fileMetaDataRepository.findFileMetaDataByUUID(UUID.fromString(fileId)));

        return new DownloadModel(new File(Paths.get(fileDir, fileId).toUri()), fileMetaDataModel);
    }
}
