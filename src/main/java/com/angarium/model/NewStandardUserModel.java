package com.angarium.model;

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
    private String username;
}
