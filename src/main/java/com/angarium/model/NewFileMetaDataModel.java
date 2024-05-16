package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Diese Klasse stellt die Metadaten zu einer neu erstellten Datei dar.
 */
@Data
@RequiredArgsConstructor
public class NewFileMetaDataModel {

    /**
     * Der Name der Datei (z.B. example.pdf).
     */
    private final String name;

    /**
     * Die maximale Anzahl der Downloads
     */
    private final int maxDownloads;

    /**
     * Maximale Anzahl von Tagen bis zur Löschung
     */
    private final int maxDays;
}
