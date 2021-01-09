package com.przemarcz.auth.service;

import com.przemarcz.auth.dto.UserDto.ActivationUserRequest;
import com.przemarcz.auth.dto.UserDto.RegisterUserRequest;
import com.przemarcz.auth.dto.UserDto.UserResponse;
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

import static com.przemarcz.auth.dto.UserDto.UpdateUserRequest;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TextMapper textMapper;
    private final MailSender mailSender;

    @Override
    @Transactional(value = "transactionManager", readOnly = true)
    public UserDetails loadUserByUsername(String value) {
        try {
            return getUserFromDatabaseById(value);
        } catch (Exception err) {
            return getUserFormDatabaseByLogin(value);
        }
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(getUserFromDatabaseById(id));
    }

    @Transactional(value = "transactionManager")
    public void register(RegisterUserRequest registerUser) throws EmailException {
        if (isUserExist(registerUser)) {
            throw new AlreadyExistException();
        }
        User user = userMapper.toUser(registerUser);
        user.generateUserActivationKey();
        userRepository.save(user);
        mailSender.sendEmail(user.getEmail(), user.getUserAuthorization().getActivationKey(), user.getLogin());
        log.info(String.format("User %s registered!", user.getLogin()));
    }

    private boolean isUserExist(RegisterUserRequest user) {
        return userRepository.findByLoginOrEmail(user.getLogin(), user.getEmail()).isPresent();
    }

    @Transactional(value = "transactionManager")
    public UserResponse updateUser(UpdateUserRequest userRequest, String userId) {
        User user = getUserFromDatabaseById(userId);
        userMapper.updateUser(user, userRequest);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional(value = "transactionManager")
    public void active(ActivationUserRequest userActivation) {
        User user = getUserFormDatabaseByLogin(userActivation.getLogin());
        user.activeAccount(userActivation.getActivationKey());
        userRepository.save(user);
    }

    public User getUserFromDatabaseById(String value) {
        return userRepository.findById(textMapper.toUUID(value)).orElseThrow(NotFoundException::new);
    }

    private User getUserFormDatabaseByLogin(String value) {
        return userRepository.findByLogin(value.toLowerCase()).orElseThrow(NotFoundException::new);
    }
}