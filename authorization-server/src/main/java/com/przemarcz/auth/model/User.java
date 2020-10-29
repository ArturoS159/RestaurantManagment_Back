package com.przemarcz.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.przemarcz.auth.exception.NotFoundException;
import com.przemarcz.auth.model.enums.RoleName;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 6529685098267757690L;

    @JsonIgnore
    private static final String PRE_ROLE = "ROLE_";
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(unique = true)
    private String email = "test";
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
    @Column(name = "identity_number")
    private String identityNumber;
    private String nip;
    private String regon;
    @Column(name = "base_role")
    @Enumerated(EnumType.STRING)
    private RoleName baseRole;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private List<UserRole> restaurantRoles = new ArrayList<>();

    public void addRole(RoleName roleName, UUID restaurantId){
        UserRole userRole = new UserRole();
        userRole.setRole(roleName);
        userRole.setRestaurantId(restaurantId);
        restaurantRoles.add(userRole);
    }

    public void delRole(RoleName roleName, UUID restaurantId){
        restaurantRoles.remove(restaurantRoles.stream().filter(userRole ->
                isRestaurantAndRoleTheSame(roleName, restaurantId, userRole)).findFirst()
                .orElseThrow(NotFoundException::new));
    }

    private boolean isRestaurantAndRoleTheSame(RoleName roleName, UUID restaurantId, UserRole userRole) {
        return userRole.getRestaurantId().equals(restaurantId) && userRole.getRole().equals(roleName);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(PRE_ROLE + baseRole));
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
        return true;
    }
}
