package com.przemarcz.auth.service

import com.przemarcz.auth.exception.AlreadyExistException
import com.przemarcz.auth.exception.NotFoundException
import com.przemarcz.auth.model.User
import com.przemarcz.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification
import spock.lang.Unroll

import static com.przemarcz.auth.dto.UserDto.*

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:bootstrap-test.yml")
class AuthServiceTest extends Specification {

    @Autowired
    AuthService authService
    @Autowired
    UserRepository userRepository

    def setup() {
        userRepository.deleteAll()
    }

    def "should load user by id or login"() {
        given:
        User user = User.builder()
                .id(UUID.randomUUID())
                .login("login")
                .email("email@test.pl")
                .build()
        userRepository.save(user)
        when:
        UserDetails byLogin = authService.loadUserByUsername("login")
        UserDetails byId = authService.loadUserByUsername(user.id.toString())
        then:
        byLogin != null
        byId != null
    }

    def "should not load user when entry value is invalid"() {
        given:
        User user = User.builder()
                .id(UUID.randomUUID())
                .login("login")
                .email("email@test.pl")
                .build()
        userRepository.save(user)
        when:
        authService.loadUserByUsername("badLogin")
        then:
        thrown NotFoundException
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
        UserResponse userResponse = authService.getUser(user1.id.toString())
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
        authService.getUser(UUID.randomUUID().toString())
        then:
        thrown NotFoundException
    }

    @Unroll
    def "should register user when all data correct"() {
        given:
        RegisterUserRequest user = RegisterUserRequest.builder()
                .login(login)
                .email(email)
                .password(password)
                .build()
        when:
        authService.register(user)
        then:
        userRepository.findAll().size() == size
        where:
        login   | email         | password   || size
        "login" | "email@wp.pl" | "password" || 1
        "logi1" | "emai1@wp.pl" | "passwor1" || 1
    }

    @Unroll
    def "should not register second user when login or email is the same"() {
        given:
        User userInDb = User.builder()
                .id(UUID.randomUUID())
                .login("login")
                .email("email@test.pl")
                .build()
        userRepository.save(userInDb)
        RegisterUserRequest user = RegisterUserRequest.builder()
                .login(login)
                .email(email)
                .password(password)
                .build()
        when:
        authService.register(user)
        then:
        thrown AlreadyExistException
        where:
        login      | email           | password   || size
        "login"    | "email@diff.pl" | "password" || 1
        "logiDiff" | "email@test.pl" | "password" || 1
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
        UserResponse userResponse = authService.updateUser(updateUserRequest, user.id.toString())
        then:
        userResponse.forename == "forename"
        userResponse.surname == "surname"
        userResponse.street == "street"
        userResponse.city == "city"
        userResponse.postCode == "10-100"
        userResponse.phoneNumber == "123456789"
        userResponse.houseNumber == "21B"
    }

    def "should active user account when all is correct"() {
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
        authService.active(activationUserRequest)
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
        authService.active(activationUserRequest)
        when:
        authService.active(activationUserRequest)
        then:
        thrown AlreadyExistException
    }

}
