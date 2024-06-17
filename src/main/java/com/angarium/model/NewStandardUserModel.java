package com.angarium.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Diese Klasse repräsentiert einen neuen Standard-Benutzer im System.
 *
 */
@Data
@RequiredArgsConstructor
public class NewStandardUserModel {

    /**
     * Der Benutzername
     */
    @NotBlank(message = "The username cannot be empty")
    private String username;
}
