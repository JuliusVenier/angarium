package com.angarium.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestHeader;
import org.jboss.resteasy.reactive.RestPath;

import java.util.List;

/**
 * Diese Klasse stellt die Metadaten zu einer neu erstellten Datei dar.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewFileMetaDataModel {

    /**
     * Der Name der Datei (z.B. example.pdf).
     */
    @RestPath
    String name;

    /**
     * Die maximale Anzahl der Downloads
     */
    @RestHeader("max-downloads")
    int maxDownloads;

    /**
     * Maximale Anzahl von Tagen bis zur Löschung
     */
    @RestHeader("max-days")
    int maxDays;

    /**
     * TODO
     */
    @RestHeader("sha256")
    String sha256;
}
