package com.angarium.repository;

import com.angarium.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {
    public UserEntity findUserByUsername(String username) {
        return find("username", username).firstResult();
    }
}
