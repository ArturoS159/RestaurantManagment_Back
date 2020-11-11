package com.przemarcz.auth.model;

import lombok.AllArgsConstructor;
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
@Table(name = "user_authorization")
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthorization implements Serializable {

    private static final long serialVersionUID = 6539685298265657691L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "activation_key")
    private String activationKey;

    public UserAuthorization(String activationKey) {
        this.activationKey = activationKey;
    }
}
