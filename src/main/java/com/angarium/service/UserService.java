package com.angarium.service;

import com.angarium.entity.UserEntity;
import com.angarium.model.NewUserModel;
import com.angarium.model.UserModel;
import com.angarium.repository.UserRepository;
import com.angarium.utils.converter.UserConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    /**
     * Erstellt einen neuen Benutzer.
     *
     * @param newUser Die Informationen des neuen Benutzers.
     * @throws IllegalArgumentException Wenn ein Benutzer mit dem gleichen Benutzernamen bereits existiert.
     */
    @Transactional
    public void createUser(NewUserModel newUser) {
        if (userExists(newUser.getUsername())){
            throw new IllegalArgumentException("A User with the username: " + newUser.getUsername() + " already exists");
        }

        UserEntity user = userConverter.toUserEntity(newUser);
        userRepository.persist(user);
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
}