package com.przemarcz.auth.service;

import com.przemarcz.auth.dto.RegisterUser;
import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.exception.NotFoundException;
import com.przemarcz.auth.exception.AlreadyExistException;
import com.przemarcz.auth.mapper.TextMapper;
import com.przemarcz.auth.mapper.UserMapper;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public UserDetails loadUserByUsername(String value) {
        try{
            return getUserById(value);
        }catch (Exception err){
            return userRepository.findByLogin(value.toLowerCase())
                    .orElseThrow(
                            () -> new NotFoundException(String.format("User %s not found!", value))
                    );
        }
    }

    @Transactional(value = "transactionManager")
    public void register(RegisterUser user) {
        if (isUserExist(user)) {
            throw new AlreadyExistException();
        }
        userRepository.save(userMapper.toUser(user));
        log.info(String.format("User %s registered!", user.getLogin()));
    }

    private boolean isUserExist(RegisterUser user) {
        return userRepository.findByLoginOrEmail(user.getLogin(), user.getEmail()).isPresent();
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public UserDto getUserData(String id) {
        return userMapper.toUserDto(getUserById(id));
    }

    private User getUserById(String value) {
        return userRepository.findById(textMapper.toUUID(value))
                .orElseThrow(
                        () -> new NotFoundException(String.format("User %s not found!", value))
                );
    }
}