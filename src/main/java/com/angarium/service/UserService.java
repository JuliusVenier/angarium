package com.angarium.service;

import com.angarium.entity.UserEntity;
import com.angarium.model.NewUserModel;
import com.angarium.model.UserModel;
import com.angarium.repository.UserRepository;
import com.angarium.utils.converter.UserConverter;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void createUser(NewUserModel newUser) {
        if (userExists(newUser.getUsername())){
            throw new IllegalArgumentException("A User with the username: " + newUser.getUsername() + " already exists");
        }

        UserEntity user = new UserEntity(
                newUser.getUsername(),
                BcryptUtil.bcryptHash(newUser.getPassword()),
                newUser.getRole());
        userRepository.persist(user);
    }

    public UserModel findByUsername(String username) {
        return UserConverter.toUserModel(userRepository.findUserByUsername(username));
    }

    public boolean userExists(String username){
        return userRepository.findUserByUsername(username) != null;
    }
}