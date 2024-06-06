package com.angarium.repository;

import com.angarium.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

    /**
     * Sucht einen Benutzer anhand seines Benutzernamens.
     *
     * @param username Der Benutzername des Benutzers.
     * @return Ein UserEntity-Objekt, das den Benutzer mit dem angegebenen Benutzernamen darstellt, oder null, wenn kein Benutzer gefunden wird.
     */
    public UserEntity findUserByUsername(String username) {
        return find("username", username).firstResult();
    }

}
