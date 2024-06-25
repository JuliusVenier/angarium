package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Diese Klasse repräsentiert die ID einer hochgeladenen Datei.
 */
@Data
@RequiredArgsConstructor
public class FileIdModel {

    /**
     * Die ID der Datei
     */
    private final String fileId;
}
