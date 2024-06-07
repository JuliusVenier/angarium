package com.angarium.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResetUserModel {
    boolean resetPassword;
    String newUsername;
}
