package com.przemarcz.auth.model;

import com.przemarcz.auth.model.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_roles")
@NoArgsConstructor
public class UserRole implements Serializable {
    private static final long serialVersionUID = 6228683028367657690L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
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
