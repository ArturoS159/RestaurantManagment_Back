package com.przemarcz.auth.domain.model;

import com.przemarcz.auth.domain.model.enums.Role;
import com.przemarcz.auth.exception.AlreadyExistException;
import com.przemarcz.auth.exception.IllegalArgumentException;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.przemarcz.auth.exception.ExceptionMessage.ACTIVATED_BEFORE;
import static com.przemarcz.auth.exception.ExceptionMessage.BAD_ACTIVATION_KEY;
import static java.util.Objects.nonNull;

@Entity
@Table(name = "users")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 6529685098267757690L;
    private static final String PRE_ROLE = "ROLE_";
    private static final int TEN = 10;

    @Id
    private UUID id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String login;
    private String password;
    private String forename;
    private String surname;
    private String street;
    private String city;
    @Column(name = "post_code")
    private String postCode;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "house_number")
    private String houseNumber;
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private final Set<UserRole> restaurantRoles = new HashSet<>();
    @Column(name = "activation_key")
    private String activationKey;
    private boolean active;


    public void addRole(Role role, UUID restaurantId) {
        UserRole userRole = new UserRole(role, restaurantId);
        restaurantRoles.add(userRole);
    }

    public void activeAccount(String activationKey) {
        if (active) {
            throw new AlreadyExistException(ACTIVATED_BEFORE);
        } else if (isActivationKeyTheSame(activationKey)) {
            active = true;
        } else {
            throw new IllegalArgumentException(BAD_ACTIVATION_KEY);
        }
    }

    private boolean isActivationKeyTheSame(String activationKey) {
        return nonNull(activationKey) && this.activationKey.equals(activationKey);
    }

    public void generateUserActivationKey() {
        activationKey = generateKey();
    }

    private String generateKey() {
        return RandomStringUtils.randomAlphabetic(TEN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return restaurantRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority((PRE_ROLE + userRole.getRole() + "_" + userRole.getRestaurantId())))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
