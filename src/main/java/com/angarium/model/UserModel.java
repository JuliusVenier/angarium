package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Diese Klasse repräsentiert einen Benutzer im System.
 */
@Data
@RequiredArgsConstructor
public class UserModel {

    /**
     * Eindeutige ID des Benutzers.
     */
    private final long id;

    /**
     * Benutzername des Benutzers.
     */
    private final String username;

    /**
     * Rolle des Benutzers.
     */
    private final String role;
}

