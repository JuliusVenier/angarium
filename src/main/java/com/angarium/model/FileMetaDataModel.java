package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Diese Klasse repräsentiert die Metadaten einer hochgeladenen Datei.
 */
@Data
@RequiredArgsConstructor
public class FileMetaDataModel {

    /**
     * Eindeutige ID der Datei.
     */
    private final UUID id;

    /**
     * Der Name der Datei (z.B. example.pdf).
     */
    private final String name;

    /**
     * Die maximalen Downloads einer Datei
     */
    private final int maxDownloads;

    /**
     * Die momentanen Downloads einer Datei
     */
    private final int currentDownloads;

    /**
     * Der Zeitpunkt des Datei-Uploads
     */
    private final LocalDate creationDate;

    /**
     * Zeitpunkt der Löschung der Datei
     */
    private final LocalDate deletionDate;

    /**
     * Der Hash einer unverschlüsselten Datei
     */
    private final String sha256;

    /**
     * Zeigt an ob die Datei verschlüsselt ist
     */
    private final boolean encrypted;

    /**
     * Der User der die Datei hochgeladen hat
     */
    private final UserModel userModel;
}
