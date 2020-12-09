package com.przemarcz.auth.model;

import com.przemarcz.auth.exception.AlreadyExistException;
import com.przemarcz.auth.exception.NotFoundException;
import com.przemarcz.auth.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 6529685098267757690L;
    private static final String PRE_ROLE = "ROLE_";
    private static final Integer TEN = 10;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String login;
    @NotNull
    @Size(min = 3, message = "aaa")
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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<UserRole> restaurantRoles = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_authorization")
    private UserAuthorization userAuthorization;
    private boolean active;


    public void addRole(Role role, UUID restaurantId) {
        UserRole userRole = new UserRole(role, restaurantId);
        restaurantRoles.add(userRole);
    }

    public void delRole(Role role, UUID restaurantId) {
        restaurantRoles.remove(restaurantRoles.stream().filter(userRole ->
                isRestaurantAndRoleTheSame(role, restaurantId, userRole)).findFirst()
                .orElseThrow(NotFoundException::new));
    }

    private boolean isRestaurantAndRoleTheSame(Role role, UUID restaurantId, UserRole userRole) {
        return userRole.getRestaurantId().equals(restaurantId) && userRole.getRole() == (role);
    }

    public void activeAccount(String activationKey) {
        if (active) {
            throw new AlreadyExistException("User was activated!");
        } else if (isActivationKeyTheSame(activationKey)) {
            active = true;
        } else {
            throw new IllegalArgumentException("Bad activation key!");
        }
    }

    private boolean isActivationKeyTheSame(String activationKey) {
        return nonNull(activationKey) && userAuthorization.getActivationKey().equals(activationKey);
    }

    public void generateUserActivationKey() {
        userAuthorization = new UserAuthorization(generateKey());
    }

    private String generateKey() {
        return RandomStringUtils.randomAlphabetic(TEN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        restaurantRoles.forEach(
                userRole -> grantedAuthorities.add(
                        new SimpleGrantedAuthority(PRE_ROLE + userRole.getRole() + "_" + userRole.getRestaurantId())
                )
        );
        return grantedAuthorities;
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
