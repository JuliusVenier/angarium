package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class UserModel {
    private final long id;
    private final String username;
    private final String role;
}
