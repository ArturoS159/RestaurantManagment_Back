package com.przemarcz.auth.service;

import com.przemarcz.auth.domain.mapper.TextMapper;
import com.przemarcz.auth.domain.mapper.UserMapper;
import com.przemarcz.auth.domain.model.User;
import com.przemarcz.auth.domain.repository.UserRepository;
import com.przemarcz.auth.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.przemarcz.auth.domain.dto.UserDto.*;
import static com.przemarcz.auth.exception.ExceptionMessage.RECORD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TextMapper textMapper;

    @Transactional(value = "transactionManager", readOnly = true)
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(getUserFromDatabaseById(id));
    }

    @Transactional(value = "transactionManager")
    public void active(ActivationUserRequest userActivation) {
        User user = getUserFormDatabaseByLogin(userActivation.getLogin());
        user.activeAccount(userActivation.getActivationKey());
        userRepository.save(user);
    }

    @Transactional(value = "transactionManager")
    public UserResponse updateUser(UpdateUserRequest userRequest, String userId) {
        User user = getUserFromDatabaseById(userId);
        userMapper.updateUser(user, userRequest);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public User getUserFromDatabaseById(String value) {
        return userRepository.findById(textMapper.toUUID(value)).orElseThrow(() -> new NotFoundException(RECORD_NOT_FOUND));
    }

    public User getUserFormDatabaseByLogin(String value) {
        return userRepository.findByLogin(value.toLowerCase()).orElseThrow(() -> new NotFoundException(RECORD_NOT_FOUND));
    }
}
