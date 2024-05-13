package com.angarium.service;

import com.angarium.entity.UserEntity;
import com.angarium.model.NewUserModel;
import com.angarium.repository.UserRepository;
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
        UserEntity user = new UserEntity(
                newUser.getUsername(),
                BcryptUtil.bcryptHash(newUser.getPassword()),
                newUser.getRole());
        userRepository.persist(user);
    }
}
