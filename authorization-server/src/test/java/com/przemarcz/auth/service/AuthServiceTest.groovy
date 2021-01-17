package com.przemarcz.auth.service


import com.przemarcz.auth.domain.model.User
import com.przemarcz.auth.domain.repository.UserRepository
import com.przemarcz.auth.exception.AlreadyExistException
import com.przemarcz.auth.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification
import spock.lang.Unroll

import static com.przemarcz.auth.domain.dto.UserDto.RegisterUserRequest

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

}
