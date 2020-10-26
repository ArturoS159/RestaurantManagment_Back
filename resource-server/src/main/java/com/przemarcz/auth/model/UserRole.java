package com.przemarcz.auth.model;

import java.io.Serializable;
import java.util.UUID;


class UserRole implements Serializable {
    private static final long serialVersionUID = 6228683028367657690L;
    private UUID userId;
    private RoleName role;
    private UUID restaurantId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public RoleName getRole() {
        return role;
    }

    public void setRole(RoleName role) {
        this.role = role;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }
}
