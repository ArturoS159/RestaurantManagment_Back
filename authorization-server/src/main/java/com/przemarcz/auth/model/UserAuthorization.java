package com.przemarcz.auth.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "user_authorization")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class UserAuthorization implements Serializable {

    private static final long serialVersionUID = 6539685298265657691L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "activation_key")
    private String activationKey;

    public UserAuthorization(String activationKey) {
        this.activationKey = activationKey;
    }
}
