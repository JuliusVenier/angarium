package com.angarium.service;

import com.angarium.entity.FileMetaDataEntity;
import com.angarium.entity.UserEntity;
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

import java.io.File;

@ApplicationScoped
@RequiredArgsConstructor
@JBossLog
public class FileService {
    private final FileMetaDataRepository fileMetaDataRepository;
    private final UserRepository userRepository;

    @Context
    SecurityContext securityContext;

    @Transactional
    public void upload(NewFileMetaDataModel newFileMetaDataModel, File file) {
        UserEntity userEntity =  userRepository.findUserByUsername("default"); //securityContext.getUserPrincipal().getName());
        FileMetaDataEntity fileMetaDataEntity = FileMetaDataConverter.toFileMetaDataEntity(newFileMetaDataModel, userEntity);
        fileMetaDataRepository.persist(fileMetaDataEntity);


    }
}
