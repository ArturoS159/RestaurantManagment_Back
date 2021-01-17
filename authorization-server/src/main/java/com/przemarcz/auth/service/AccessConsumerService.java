package com.przemarcz.auth.service;

import com.przemarcz.auth.domain.mapper.TextMapper;
import com.przemarcz.auth.domain.model.User;
import com.przemarcz.auth.domain.model.enums.Role;
import com.przemarcz.auth.domain.repository.UserRepository;
import com.przemarcz.auth.domain.repository.UserRoleRepository;
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
    private final UserService userService;
    private final TextMapper textMapper;

    public void addOrDeleteOwnerRole(AccessAvro accessAvro) {
        final String userId = accessAvro.getUserId().toString();
        final UUID restaurantId = textMapper.toUUID(accessAvro.getRestaurantId());

        if (isRestaurantAdded(accessAvro.getType())) {
            User user = userService.getUserFromDatabaseById(userId);
            user.addRole(Role.OWNER, restaurantId);
            userRepository.save(user);
        } else {
            deleteAllRolesFromRestaurant(restaurantId);
        }
        log.info(String.format("Restaurant added to user %s", userId));
    }

    void deleteAllRolesFromRestaurant(UUID restaurantId){
        userRoleRepository.deleteByRestaurantId(restaurantId);
        log.info(String.format("Restaurant %s deleted", restaurantId));
    }

    private boolean isRestaurantAdded(AddDelete type) {
        return type.equals(AddDelete.ADD);
    }
}
