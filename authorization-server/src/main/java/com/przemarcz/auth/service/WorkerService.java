package com.przemarcz.auth.service;

import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.dto.exception.NotFoundException;
import com.przemarcz.auth.mapper.UserMapper;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.model.UserRole;
import com.przemarcz.auth.model.enums.RoleName;
import com.przemarcz.auth.repository.UserRepository;
import com.przemarcz.auth.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class WorkerService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<UserDto> getAllRestaurantWorkers(UUID restaurantId, Pageable pageable) {
        List<UUID> userIdList = getUserIdsByRestaurantId(restaurantId);

        return userRepository.findByRestaurantRolesUserIdIn(userIdList, pageable)
                .map(userMapper::toUserWorkerDto);
    }

    public List<UUID> getUserIdsByRestaurantId(UUID restaurantId) {
        return userRoleRepository.findAllByRestaurantIdAndRole(restaurantId, RoleName.WORKER)
                .stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList());
    }

    @Transactional(value = "transactionManager")
    public void addRestaurantWorker(UUID restaurantId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("User %s not found!", email))
        );
        //TODO is exist
        user.addRole(RoleName.WORKER, restaurantId);
        userRepository.save(user);
    }
}
