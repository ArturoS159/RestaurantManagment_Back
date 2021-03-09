package com.przemarcz.auth.domain.model;

import com.przemarcz.auth.domain.model.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_roles")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class UserRole implements Serializable {

    private static final long serialVersionUID = 6228683028367657690L;

    @Id
    private UUID id = UUID.randomUUID();
    @Column(name = "user_id")
    private UUID userId;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "restaurant_id")
    private UUID restaurantId;

    public UserRole(Role role, UUID restaurantId) {
        this.role = role;
        this.restaurantId = restaurantId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserRole userRole = (UserRole) object;
        return id.equals(userRole.id) && userId.equals(userRole.userId) && role == userRole.role && restaurantId.equals(userRole.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, role, restaurantId);
    }
}
