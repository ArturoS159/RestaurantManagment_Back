package com.przemarcz.auth.model;

import com.przemarcz.auth.model.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
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
    @Column(name = "id", updatable = false, nullable = false)
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
}
