package com.przemarcz.auth.service;

import com.przemarcz.auth.exception.AlreadyExistException;
import com.przemarcz.auth.exception.NotFoundException;
import com.przemarcz.auth.mapper.UserMapper;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.model.UserRole;
import com.przemarcz.auth.model.enums.Role;
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

import static com.przemarcz.auth.dto.UserDto.WorkerResponse;

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
        return userRoleRepository.findAllByRestaurantIdAndRole(restaurantId, Role.WORKER)
                .stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList());
    }

    @Transactional(value = "transactionManager")
    public WorkerResponse addRestaurantWorker(UUID restaurantId, String email) {
        User user = getUserFromDatabaseByEmail(email.toLowerCase());
        if (isWorkerAddedBefore(restaurantId, user.getId())) {
            throw new AlreadyExistException(String.format("User %s is added before!", email));
        }
        user.addRole(Role.WORKER, restaurantId);
        userRepository.save(user);
        return userMapper.toWorkerResponse(user);
    }

    private boolean isWorkerAddedBefore(UUID restaurantId, UUID userId) {
        return userRoleRepository.findByRestaurantIdAndUserIdAndRole(restaurantId, userId, Role.WORKER).isPresent();
    }

    public User getUserFromDatabaseByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                NotFoundException::new
        );
    }

    public void deleteRestaurantWorker(UUID restaurantId, UUID workerId) {
        User user = userRepository.findById(workerId).orElseThrow(
                NotFoundException::new);
        user.delRole(Role.WORKER, restaurantId);
        userRepository.save(user);
    }
}
