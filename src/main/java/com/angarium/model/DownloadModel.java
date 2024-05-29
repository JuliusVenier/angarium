package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Data
@RequiredArgsConstructor
public class DownloadModel {
    private final File file;
    private final FileMetaDataModel fileMetaDataModel;
}
