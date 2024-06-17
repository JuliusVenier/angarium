package com.angarium.service;

import com.angarium.entity.UserEntity;
import com.angarium.model.*;
import com.angarium.repository.UserRepository;
import com.angarium.utils.converter.UserConverter;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

/**
 * UserService stellt verschiedene Methoden zur Verwaltung von Benutzern bereit.
 * Diese Klasse ist für die Erstellung, Löschung, Aktualisierung und Suche von Benutzern zuständig.
 * Sie verwendet UserRepository für den Datenbankzugriff und UserConverter für die Umwandlung zwischen
 * Entitäten und Modellen.
 *
 */
@ApplicationScoped
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @ConfigProperty(name = "angarium.config.default-user-passwords")
    String defaultUserPassword;

    @Context
    SecurityContext securityContext;

    /**
     * Erstellt einen neuen Standardbenutzer.
     *
     * @param newStandardUserModel Das Modell, das die Informationen des neuen Standardbenutzers enthält.
     * @return Ein UserModel-Objekt, das die Informationen des erstellten Benutzers enthält.
     * @throws IllegalArgumentException Wenn ein Benutzer mit dem gleichen Benutzernamen bereits existiert.
     */
    public UserModel createStandardUser(NewStandardUserModel newStandardUserModel){
        return createUser(new NewUserModel(newStandardUserModel.getUsername(), defaultUserPassword, "user"));
    }
    /**
     * Erstellt einen neuen Benutzer.
     *
     * @param newUser Die Informationen des neuen Benutzers.
     * @throws IllegalArgumentException Wenn ein Benutzer mit dem gleichen Benutzernamen bereits existiert.
     */
    @Transactional
    public UserModel createUser(NewUserModel newUser) {
        if (userExists(newUser.getUsername())){
            throw new IllegalArgumentException("A User with the username: " + newUser.getUsername() + " already exists");
        }

        UserEntity user = userConverter.toUserEntity(newUser);
        userRepository.persist(user);
        return userConverter.toUserModel(user);
    }

    /**
     * Löscht einen Benutzer anhand seiner ID.
     *
     * @param id Die ID des zu löschenden Benutzers.
     */
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Sucht nach einem Benutzer anhand seines Benutzernamens.
     *
     * @param username Der Benutzername des gesuchten Benutzers.
     * @return Ein UserModel-Objekt, das die Informationen des gefundenen Benutzers enthält,
     *         oder null, wenn kein Benutzer mit dem angegebenen Benutzernamen gefunden wurde.
     */
    public UserModel findByUsername(String username) {
        return userConverter.toUserModel(userRepository.findUserByUsername(username));
    }

    /**
     * Gibt eine Liste aller Benutzer zurück.
     *
     * @return Eine Liste von UserModel-Objekten, die die Informationen aller Benutzer enthalten.
     */
    public List<UserModel> listAllUsers() {
        return userRepository.listAll().stream()
                .map(userConverter::toUserModel)
                .toList();
    }

    /**
     * Prüft, ob ein Benutzer mit dem angegebenen Benutzernamen existiert.
     *
     * @param username Der Benutzername des zu überprüfenden Benutzers.
     * @return true, wenn ein Benutzer mit dem angegebenen Benutzernamen existiert, false sonst.
     */
    public boolean userExists(String username){
        return userRepository.findUserByUsername(username) != null;
    }

    /**
     * Setzt das Passwort eines Benutzers zurück.
     *
     * @param id Die ID des Benutzers, dessen Passwort zurückgesetzt werden soll.
     */
    @Transactional
    public void resetUserPassword(Long id) {
        UserEntity userEntity = userRepository.findById(id);
        userEntity.setPassword(BcryptUtil.bcryptHash(defaultUserPassword));

        userRepository.persist(userEntity);
    }

    /**
     * Aktualisiert die Informationen eines Benutzers.
     *
     * @param updateUserModel Das Modell, das die neuen Informationen des Benutzers enthält.
     */
    @Transactional
    public void updateUser(UpdateUserModel updateUserModel) {
        UserEntity userEntity = userRepository.findUserByUsername(securityContext.getUserPrincipal().getName());

        if (!StringUtils.isBlank(updateUserModel.getUsername())) {
            userEntity.setUsername(updateUserModel.getUsername());
        }

        if (!StringUtils.isBlank(updateUserModel.getPassword())) {
            userEntity.setPassword(BcryptUtil.bcryptHash(updateUserModel.getPassword()));
        }

        userRepository.persist(userEntity);
    }
}