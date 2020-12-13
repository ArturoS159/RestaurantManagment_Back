package com.przemarcz.auth.service;

import com.przemarcz.auth.exception.NotFoundException;
import com.przemarcz.auth.mapper.TextMapper;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.model.enums.Role;
import com.przemarcz.auth.repository.UserRepository;
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
    private final TextMapper textMapper;

    public void addOrDeleteOwnerRole(AccessAvro accessAvro) {
        final UUID userId = textMapper.toUUID(accessAvro.getUserId());
        final UUID restaurantId = textMapper.toUUID(accessAvro.getRestaurantId());

        User user = getUserFromDatabase(userId);
        if (isRestaurantAdded(accessAvro.getType())) {
            user.addRole(Role.OWNER, restaurantId);
        } else {
            user.delRole(Role.OWNER, restaurantId);
        }
        userRepository.save(user);
        log.info(String.format("Owner role update to user: %s", userId));
    }

    private boolean isRestaurantAdded(AddDelete type) {
        return type.equals(AddDelete.ADD);
    }

    private User getUserFromDatabase(UUID userId) {
        return userRepository.findById(userId).orElseThrow(NotFoundException::new);
    }
}
