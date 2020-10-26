package com.przemarcz.auth.repository;


import com.przemarcz.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByLoginOrId(@Param("login") String login, @Param("id") UUID id);

    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByLoginOrEmail(@Param("login") String login, @Param("email") String email);

    Page<User> findByRestaurantRolesUserIdIn(@Param("userIdList") List<UUID> userIdList, Pageable pageable);
}
