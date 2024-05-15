package com.angarium.utils.converter;

import com.angarium.entity.UserEntity;
import com.angarium.model.UserModel;

public class UserConverter {
    public static UserModel toUserModel(UserEntity user) {
        if (user == null) {
            return null;
        }

        return new UserModel(user.id, user.getUsername(), user.getPassword());
    }
}
