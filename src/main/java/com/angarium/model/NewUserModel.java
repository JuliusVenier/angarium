package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class NewUserModel {
    private final String username;
    private final String password;
    private final String role;
}
