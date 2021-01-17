package com.przemarcz.auth.service;

import com.przemarcz.auth.domain.dto.UserDto.RegisterUserRequest;
import com.przemarcz.auth.domain.mapper.UserMapper;
import com.przemarcz.auth.domain.model.User;
import com.przemarcz.auth.domain.repository.UserRepository;
import com.przemarcz.auth.exception.AlreadyExistException;
import com.przemarcz.auth.util.MailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.przemarcz.auth.exception.ExceptionMessage.RECORD_ALREADY_EXIST;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MailSender mailSender;
    private final UserService userService;

    @Override
    @Transactional(value = "transactionManager", readOnly = true)
    public UserDetails loadUserByUsername(String value) {
        try {
            return userService.getUserFromDatabaseById(value);
        } catch (Exception err) {
            return userService.getUserFormDatabaseByLogin(value);
        }
    }

    @Transactional(value = "transactionManager")
    public void register(RegisterUserRequest registerUser) throws EmailException {
        if (isUserExist(registerUser)) {
            throw new AlreadyExistException(RECORD_ALREADY_EXIST);
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
}