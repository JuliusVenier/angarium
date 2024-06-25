package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Diese Klasse repräsentiert ein Update der Benutzerdaten eines Benutzers im System.
 */
@Data
@RequiredArgsConstructor
public class UpdateUserModel {

    /**
     * Benutzername des Benutzers.
     */
    private final String username;


    /**
     * Eindeutiger Benutzername des Benutzers.
     */
    private final String password;
}
