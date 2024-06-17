package com.angarium.model;

import com.angarium.constraint.MaxDays;
import com.angarium.constraint.MaxDownloads;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "The filename cannot be blank")
    @RestPath
    String name;

    /**
     * Die maximale Anzahl der Downloads
     */
    @MaxDownloads
    @RestHeader("max-downloads")
    Integer maxDownloads;

    /**
     * Maximale Anzahl von Tagen bis zur Löschung
     */
    @MaxDays
    @RestHeader("max-days")
    Integer maxDays;

    /**
     * Zeigt an ob die Datei verschlüsselt wurde
     */
    @RestHeader("encrypted")
    Boolean encrypted;

    /**
     * Der Hash der unverschlüsselten Datei
     */
    @RestHeader("sha256")
    String sha256;
}
