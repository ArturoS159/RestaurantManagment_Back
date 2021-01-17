package com.przemarcz.auth.service


import com.przemarcz.auth.domain.model.User
import com.przemarcz.auth.domain.repository.UserRepository
import com.przemarcz.auth.exception.AlreadyExistException
import com.przemarcz.auth.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static com.przemarcz.auth.domain.dto.UserDto.*

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:bootstrap-test.yml")
class UserServiceTest extends Specification {

    @Autowired
    UserService userService
    @Autowired
    AuthService authService
    @Autowired
    UserRepository userRepository

    def setup() {
        userRepository.deleteAll()
    }

    def "should get user by id"() {
        given:
        User user = User.builder()
                .id(UUID.randomUUID())
                .login("login")
                .email("email@test.pl")
                .build()
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .login("login1")
                .email("email1@test.pl")
                .build()
        userRepository.saveAll(Arrays.asList(user, user1))
        when:
        UserResponse userResponse = userService.getUser(user1.id.toString())
        then:
        userResponse.login == "login1"
        userResponse.email == "email1@test.pl"
    }

    def "should not get user by id when id is wrong"() {
        given:
        User user = User.builder()
                .id(UUID.randomUUID())
                .login("login")
                .email("email@test.pl")
                .build()
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .login("login1")
                .email("email1@test.pl")
                .build()
        userRepository.saveAll(Arrays.asList(user, user1))
        when:
        userService.getUser(UUID.randomUUID().toString())
        then:
        thrown NotFoundException
    }

    def "should update user"() {
        given:
        User user = User.builder()
                .id(UUID.randomUUID())
                .login("login")
                .email("email@test.pl")
                .build()
        userRepository.save(user)
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .forename("forename")
                .surname("surname")
                .street("street")
                .city("city")
                .postCode("10-100")
                .phoneNumber("123456789")
                .houseNumber("21B")
                .build()
        when:
        UserResponse userResponse = userService.updateUser(updateUserRequest, user.id.toString())
        then:
        userResponse.forename == "forename"
        userResponse.surname == "surname"
        userResponse.street == "street"
        userResponse.city == "city"
        userResponse.postCode == "10-100"
        userResponse.phoneNumber == "123456789"
        userResponse.houseNumber == "21B"
    }

    def 'should active user account when key is correct'() {
        given:
        RegisterUserRequest user = RegisterUserRequest.builder()
                .login("login")
                .email("email@test.pl")
                .password("password")
                .build()
        authService.register(user)
        when:
        String key = userRepository.findAll().get(0).getUserAuthorization().activationKey
        ActivationUserRequest activationUserRequest = new ActivationUserRequest("login", key)
        userService.active(activationUserRequest)
        then:
        userRepository.findAll().get(0).active
    }

    def "should not active user account when user is activated before"() {
        given:
        RegisterUserRequest user = RegisterUserRequest.builder()
                .login("login")
                .email("email@test.pl")
                .password("password")
                .build()
        authService.register(user)
        String key = userRepository.findAll().get(0).getUserAuthorization().activationKey
        ActivationUserRequest activationUserRequest = new ActivationUserRequest("login", key)
        userService.active(activationUserRequest)
        when:
        userService.active(activationUserRequest)
        then:
        thrown AlreadyExistException
    }
}
