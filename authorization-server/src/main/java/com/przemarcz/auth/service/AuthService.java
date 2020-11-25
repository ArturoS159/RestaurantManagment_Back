package com.przemarcz.auth.service;

import com.przemarcz.auth.dto.RegisterUser;
import com.przemarcz.auth.dto.UserActivation;
import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.exception.AlreadyExistException;
import com.przemarcz.auth.exception.NotFoundException;
import com.przemarcz.auth.util.MailSender;
import com.przemarcz.auth.mapper.TextMapper;
import com.przemarcz.auth.mapper.UserMapper;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.repository.UserRepository;
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
        try{
            return getUserFromDbById(value);
        }catch (Exception err){
            return getUserFormDbByLogin(value);
        }
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public UserDto getUser(String id) {
        return userMapper.toUserDto(getUserFromDbById(id));
    }

    @Transactional(value = "transactionManager")
    public void register(RegisterUser registerUser) throws EmailException {
        if (isUserExist(registerUser)) {
            throw new AlreadyExistException();
        }
        User user = userMapper.toUser(registerUser);
        user.generateUserActivationKey();
        mailSender.sendEmail(user.getEmail(),user.getUserAuthorization().getActivationKey(),user.getLogin());
        userRepository.save(user);
        log.info(String.format("User %s registered!", user.getLogin()));
    }

    private boolean isUserExist(RegisterUser user) {
        return userRepository.findByLoginOrEmail(user.getLogin(), user.getEmail()).isPresent();
    }

    private User getUserFromDbById(String value) {
        return userRepository.findById(textMapper.toUUID(value))
                .orElseThrow(
                        () -> new NotFoundException(String.format("User %s not found!", value))
                );
    }

    private User getUserFormDbByLogin(String value) {
        return userRepository.findByLogin(value.toLowerCase())
                .orElseThrow(
                        () -> new NotFoundException(String.format("User %s not found!", value))
                );
    }

    @Transactional(value = "transactionManager")
    public void updateUser(String userId, UserDto userDto) {
        User user = getUserFromDbById(userId);
        userMapper.updateUser(user,userDto);
        userRepository.save(user);
    }

    @Transactional(value = "transactionManager")
    public void active(UserActivation userActivation) {
        User user = getUserFormDbByLogin(userActivation.getLogin());
        user.activeAccount(userActivation.getActivationKey());
        userRepository.save(user);
    }
}