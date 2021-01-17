package com.przemarcz.auth.domain.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    private UUID id = UUID.randomUUID();
    @Column(name = "activation_key")
    private String activationKey;

    public UserAuthorization(String activationKey) {
        this.activationKey = activationKey;
    }
}
