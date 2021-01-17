package com.przemarcz.auth.service;

import com.przemarcz.auth.domain.mapper.UserMapper;
import com.przemarcz.auth.domain.model.User;
import com.przemarcz.auth.domain.model.UserRole;
import com.przemarcz.auth.domain.model.enums.Role;
import com.przemarcz.auth.domain.repository.UserRepository;
import com.przemarcz.auth.domain.repository.UserRoleRepository;
import com.przemarcz.auth.exception.AlreadyExistException;
import com.przemarcz.auth.exception.ExceptionMessage;
import com.przemarcz.auth.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.przemarcz.auth.domain.dto.UserDto.WorkerResponse;
import static com.przemarcz.auth.exception.ExceptionMessage.RECORD_ALREADY_EXIST;
import static com.przemarcz.auth.exception.ExceptionMessage.RECORD_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class WorkerService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<WorkerResponse> getAllRestaurantWorkers(UUID restaurantId, Pageable pageable) {
        List<UUID> workersId = getWorkersByRestaurantId(restaurantId);

        return userRepository.findByIdIn(workersId, pageable)
                .map(userMapper::toWorkerResponse);
    }

    private List<UUID> getWorkersByRestaurantId(UUID restaurantId) {
        return userRoleRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .filter(userRole -> userRole.getRole().equals(Role.WORKER))
                .map(UserRole::getUserId)
                .collect(Collectors.toList());
    }

    @Transactional(value = "transactionManager")
    public WorkerResponse addRestaurantWorker(UUID restaurantId, String email) {
        User user = getUserFromDatabaseByEmail(email.toLowerCase());
        if (isWorkerAddedBefore(restaurantId, user.getId())) {
            throw new AlreadyExistException(RECORD_ALREADY_EXIST);
        }
        user.addRole(Role.WORKER, restaurantId);
        userRepository.save(user);
        return userMapper.toWorkerResponse(user);
    }

    private boolean isWorkerAddedBefore(UUID restaurantId, UUID userId) {
        return userRoleRepository.findAllByRestaurantId(restaurantId).stream()
                .filter(userRole -> userRole.getRole().equals(Role.WORKER))
                .anyMatch(userRole -> userRole.getUserId().equals(userId));
    }

    private User getUserFromDatabaseByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ExceptionMessage.RECORD_NOT_FOUND));
    }

    @Transactional(value = "transactionManager")
    public void deleteRestaurantWorker(UUID restaurantId, UUID workerId) {
        UserRole workerAuth = userRoleRepository.findAllByRestaurantId(restaurantId).stream()
                .filter(userRole -> userRole.getRole().equals(Role.WORKER))
                .filter(userRole -> userRole.getUserId().equals(workerId))
                .findAny()
                .orElseThrow(() -> new NotFoundException(RECORD_NOT_FOUND));
        userRoleRepository.delete(workerAuth);
    }
}
