package com.przemarcz.auth.repository;

import com.przemarcz.auth.model.UserRole;
import com.przemarcz.auth.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    List<UserRole> findAllByRestaurantIdAndRole(@Param("restaurantId") UUID restaurantId, @Param("role") Role role);
}
