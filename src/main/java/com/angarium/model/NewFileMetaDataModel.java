package com.angarium.model;

import jakarta.ws.rs.DefaultValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.reactive.RestHeader;
import org.jboss.resteasy.reactive.RestPath;

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
    Integer maxDownloads;

    /**
     * Maximale Anzahl von Tagen bis zur Löschung
     */
    @RestHeader("max-days")
    Integer maxDays;

    /**
     *
     */
    @RestHeader("encrypted")
    Boolean encrypted;

    /**
     * TODO
     */
    @RestHeader("sha256")
    String sha256;
}
