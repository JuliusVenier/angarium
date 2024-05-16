package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
/**
 * Diese Klasse repräsentiert einen neuen Benutzer im System.
 *
 */
@Data
@RequiredArgsConstructor
public class NewUserModel {

    /**
     * Benutzername des Benutzers.
     */
    private final String username;


    /**
     * Eindeutiger Benutzername des Benutzers.
     */
    private final String password;

    /**
     * Rolle des Benutzers.
     */
    private final String role;
}
