package com.przemarcz.auth.model;

import com.przemarcz.auth.model.enums.Role;

import java.io.Serializable;
import java.util.UUID;


class UserRole implements Serializable {

    private static final long serialVersionUID = 6228683028367657690L;
    private UUID userId;
    private Role role;
    private UUID restaurantId;

    public UUID getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }
}
