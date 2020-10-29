package com.przemarcz.auth.service;

import com.przemarcz.auth.dto.RegisterPersonalData;
import com.przemarcz.auth.dto.RegisterUser;
import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.exception.NotFoundException;
import com.przemarcz.auth.exception.UserAlreadyExistException;
import com.przemarcz.auth.mapper.UserMapper;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String login) {
        return userRepository.findByLoginOrId(login,getUserIdInUUID(login))
                .orElseThrow(
                        () -> new NotFoundException(String.format("User %s not found!", login))
        );
    }

    @Transactional(value = "transactionManager")
    public void register(RegisterUser registerUser) {
        if (isUserExist(registerUser)) {
            throw new UserAlreadyExistException();
        }
        userRepository.save(userMapper.toUser(registerUser,registerUser.getRole()));
        log.info(String.format("User %s registered!", registerUser.getLogin()));
    }

    private boolean isUserExist(RegisterUser user) {
        return userRepository.findByLoginOrEmail(user.getLogin(), user.getEmail()).isPresent();
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public UserDto getUserData(String id) {
        return userMapper.toUserDto(
                userRepository.findById(requireNonNull(getUserIdInUUID(id)))
                        .orElseThrow(NotFoundException::new)
        );
    }

    private UUID getUserIdInUUID(String userId) {
        try{
            return UUID.fromString(userId);
        }catch (Exception err){
            return null;
        }
    }

    @Transactional(value = "transactionManager")
    public void continueRegister(RegisterPersonalData registerPersonalData, String login) {
        User user = userRepository.findByLogin(login).orElseThrow(() ->
                new NotFoundException(String.format("User %s not found!",login)));
        userMapper.addPersonalData(user,registerPersonalData);
        userRepository.save(user);
    }
}
