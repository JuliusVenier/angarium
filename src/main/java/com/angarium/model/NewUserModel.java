package com.angarium.model;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "The username cannot be empty")
    private final String username;


    /**
     * Eindeutiger Benutzername des Benutzers.
     */
    @NotBlank(message = "The password cannot be empty")
    private final String password;

    /**
     * Rolle des Benutzers.
     */
    @NotBlank(message = "The role cannot be empty")
    private final String role;
}
