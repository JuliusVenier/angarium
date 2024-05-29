package com.angarium.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Diese Klasse repräsentiert die Metadaten einer hochgeladenen Datei.
 * Sie wird mit JPA persistiert und verwendet, um die Metadaten einer Datei in der Datenbank zu speichern und abzurufen.
 *
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "file-meta-data")
public class FileMetaDataEntity {

    /**
     * Eindeutige ID der Datei.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Der Name der Datei (z.B. example.pdf).
     */
    @Column(nullable = false)
    private String name;

    /**
     * Die maximalen Downloads einer Datei
     */
    @Column(name = "max-downloads", nullable = false, updatable = false)
    private int maxDownloads;

    /**
     * Die momentanen Downloads einer Datei
     */
    @Column(name = "current-downloads")
    private int currentDownloads;

    /**
     * Der Zeitpunkt des Datei-Uploads
     */
    @Column(name = "creation-date", nullable = false, updatable = false)
    private LocalDate creationDate;

    /**
     * Zeitpunkt der Löschung der Datei
     */
    @Column(name = "deletion-date", nullable = false, updatable = false)
    private LocalDate deletionDate;

    /**
     * TODO
     */
    private String sha256;

    /**
     * TODO
     */
    private boolean encrypted;

    /**
     * Der User der die Datei hochgeladen hat
     */
    @ManyToOne
    @JoinColumn(name = "angarium_user", nullable = false)
    private UserEntity userEntity;
}
