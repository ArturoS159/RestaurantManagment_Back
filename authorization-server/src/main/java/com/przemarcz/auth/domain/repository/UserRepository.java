package com.przemarcz.auth.domain.repository;

import com.przemarcz.auth.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.login=:value OR u.email=:value")
    Optional<User> findByLoginOrEmail(String value);

    Optional<User> findByLoginOrEmail(String login, String email);

    Page<User> findByIdIn(List<UUID> userIdList, Pageable pageable);
}
