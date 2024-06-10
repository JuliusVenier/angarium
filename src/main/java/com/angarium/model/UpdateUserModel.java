package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

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
