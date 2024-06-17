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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("Creating standard user");

        if(StringUtils.isBlank(defaultUserPassword)) {
            log.warn("Default user password is blank");
        }

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
        log.info("Creating new user");

        if (userExists(newUser.getUsername())){
            log.error("A User with the username: \" + newUser.getUsername() + \" already exists\"");
            throw new IllegalArgumentException("A User with the username: " + newUser.getUsername() + " already exists");
        }

        UserEntity user = userConverter.toUserEntity(newUser);
        userRepository.persist(user);

        log.info("A new user with the username {} has been created.", newUser.getUsername());
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
        log.info("Deleted user with id: {}", id);
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
     * @return
     */
    @Transactional
    public UserModel resetUserPassword(Long id) {
        log.info("Resetting password for user with id: {}", id);

        UserEntity userEntity = userRepository.findById(id);
        userEntity.setPassword(BcryptUtil.bcryptHash(defaultUserPassword));

        userRepository.persist(userEntity);

        log.info("Password reset for user with id: {} was successful", id);
        return userConverter.toUserModel(userEntity);
    }

    /**
     * Aktualisiert die Informationen eines Benutzers.
     *
     * @param updateUserModel Das Modell, das die neuen Informationen des Benutzers enthält.
     * @return
     */
    @Transactional
    public UserModel updateUser(UpdateUserModel updateUserModel) {
        log.info("Updating user with name {}", securityContext.getUserPrincipal().getName());

        UserEntity userEntity = userRepository.findUserByUsername(securityContext.getUserPrincipal().getName());

        if (!StringUtils.isBlank(updateUserModel.getUsername())) {
            log.info("Updating username from user with username {} to {}", securityContext.getUserPrincipal().getName(), updateUserModel.getUsername());
            userEntity.setUsername(updateUserModel.getUsername());
        }

        if (!StringUtils.isBlank(updateUserModel.getPassword())) {
            log.info("Updating password from user with username {}", securityContext.getUserPrincipal().getName());
            userEntity.setPassword(BcryptUtil.bcryptHash(updateUserModel.getPassword()));
        }

        userRepository.persist(userEntity);
        return userConverter.toUserModel(userEntity);
    }
}