package com.przemarcz.auth.service;

import com.przemarcz.auth.dto.UserDto.UserActivationRequest;
import com.przemarcz.auth.dto.UserDto.UserRegisterRequest;
import com.przemarcz.auth.dto.UserDto.UserResponse;
import com.przemarcz.auth.dto.UserDto.UserUpdateRequest;
import com.przemarcz.auth.exception.AlreadyExistException;
import com.przemarcz.auth.exception.NotFoundException;
import com.przemarcz.auth.mapper.TextMapper;
import com.przemarcz.auth.mapper.UserMapper;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.repository.UserRepository;
import com.przemarcz.auth.util.MailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TextMapper textMapper;
    private final MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String value) {
        try {
            return getUserFromDatabaseById(value);
        } catch (Exception err) {
            return getUserFormDbByLogin(value);
        }
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public UserResponse getUser(String id) {
        return userMapper.toUserDto(getUserFromDatabaseById(id));
    }

    @Transactional(value = "transactionManager")
    public void register(UserRegisterRequest registerUser) throws EmailException {
        if (isUserExist(registerUser)) {
            throw new AlreadyExistException();
        }
        User user = userMapper.toUser(registerUser);
        user.generateUserActivationKey();
        userRepository.save(user);
        mailSender.sendEmail(user.getEmail(), user.getUserAuthorization().getActivationKey(), user.getLogin());
        log.info(String.format("User %s registered!", user.getLogin()));
    }

    private boolean isUserExist(UserRegisterRequest user) {
        return userRepository.findByLoginOrEmail(user.getLogin(), user.getEmail()).isPresent();
    }

    @Transactional(value = "transactionManager")
    public UserResponse updateUser(UserUpdateRequest userRequest, String userId) {
        User user = getUserFromDatabaseById(userId);
        userMapper.updateUser(user, userRequest);
        return userMapper.toUserDto(userRepository.save(user));
    }

    public User getUserFromDatabaseById(String value) {
        return userRepository.findById(textMapper.toUUID(value)).orElseThrow(NotFoundException::new);
    }

    @Transactional(value = "transactionManager")
    public void active(UserActivationRequest userActivation) {
        User user = getUserFormDbByLogin(userActivation.getLogin());
        user.activeAccount(userActivation.getActivationKey());
        userRepository.save(user);
    }

    private User getUserFormDbByLogin(String value) {
        return userRepository.findByLogin(value.toLowerCase()).orElseThrow(NotFoundException::new);
    }
}