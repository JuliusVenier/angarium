package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.File;

/**
 * Diese Klasse repräsentiert eine Datei die heruntergeladen wird.
 */
@Data
@RequiredArgsConstructor
public class DownloadModel {

    /**
     * Die Datei die heruntergeladen wird
     */
    private final File file;

    /**
     * Die Meta Daten der Datei
     */
    private final FileMetaDataModel fileMetaDataModel;
}
