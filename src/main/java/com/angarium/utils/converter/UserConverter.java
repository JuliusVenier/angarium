package com.angarium.utils.converter;

import com.angarium.entity.UserEntity;
import com.angarium.model.UserModel;

public class UserConverter {

    /**
     * Konvertiert eine UserEntity in ein UserModel-Objekt.
     *
     * @param user Die zu konvertierende UserEntity.
     * @return Ein neues UserModel-Objekt, das die Daten der UserEntity enthält (außer das Passwort), oder null, wenn die UserEntity null ist.
     */
    public static UserModel toUserModel(UserEntity user) {
        if (user == null) {
            return null;
        }

        return new UserModel(user.id, user.getUsername(), user.getPassword());
    }
}
