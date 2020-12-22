package com.przemarcz.auth.service;

import com.przemarcz.auth.mapper.TextMapper;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.model.enums.Role;
import com.przemarcz.auth.repository.UserRepository;
import com.przemarcz.auth.repository.UserRoleRepository;
import com.przemarcz.avro.AccessAvro;
import com.przemarcz.avro.AddDelete;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessConsumerService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuthService authService;
    private final TextMapper textMapper;

    public void addOrDeleteOwnerRole(AccessAvro accessAvro) {
        final String userId = accessAvro.getUserId().toString();
        final UUID restaurantId = textMapper.toUUID(accessAvro.getRestaurantId());

        if (isRestaurantAdded(accessAvro.getType())) {
            User user = authService.getUserFromDatabaseById(userId);
            user.addRole(Role.OWNER, restaurantId);
            userRepository.save(user);
        } else {
            deleteAllRolesFromRestaurant(restaurantId);
        }
        log.info(String.format("Owner role update to user: %s", userId));
    }

    void deleteAllRolesFromRestaurant(UUID restaurantId){
        userRoleRepository.deleteByRestaurantId(restaurantId);
    }

    private boolean isRestaurantAdded(AddDelete type) {
        return type.equals(AddDelete.ADD);
    }
}
