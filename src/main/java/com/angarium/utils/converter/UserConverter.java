package com.angarium.utils.converter;

import com.angarium.entity.UserEntity;
import com.angarium.model.NewUserModel;
import com.angarium.model.UserModel;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@ApplicationScoped
public class UserConverter {

    /**
     * Konvertiert eine 'UserEntity' in ein UserModel-Objekt.
     *
     * @param user Die zu konvertierende UserEntity.
     * @return Ein neues UserModel-Objekt, das die Daten der UserEntity enthält (außer das Passwort)
     */
    public UserModel toUserModel(@NonNull UserEntity user) {

        return new UserModel(user.id, user.getUsername(), user.getRole());
    }

    /**
     * Konvertiert eine NewUserModel in ein UserEntity-Objekt.
     *
     * @param newUser Die zu konvertierende NewUserModel.
     * @return Ein neues UserEntity-Objekt, das die Daten der NewUserModel enthält
     */
    public UserEntity toUserEntity(@NonNull NewUserModel newUser) {

        return new UserEntity(newUser.getUsername(), BcryptUtil.bcryptHash(newUser.getPassword()), newUser.getRole());
    }
}
