package com.przemarcz.auth.repository;

import com.przemarcz.auth.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    List<UserRole> findAllByRestaurantId(UUID restaurantId);

    void deleteByRestaurantId(UUID restaurantId);
}
