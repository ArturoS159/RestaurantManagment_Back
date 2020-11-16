package com.przemarcz.auth.service

import com.przemarcz.auth.dto.RegisterUser
import com.przemarcz.auth.exception.AlreadyExistException
import com.przemarcz.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification
import spock.lang.Unroll

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

    RegisterUser prepareRegisterUser(String login, String email, String password) {
        RegisterUser user = new RegisterUser()
        user.login = login
        user.email = email
        user.password = password
        return user
    }

    @Unroll
    def "should create User"() {
        given:
        RegisterUser registerUser = prepareRegisterUser(login, email, password)
        when:
        authService.register(registerUser)
        then:
        userRepository.findAll().size() == size
        where:
        login   | email         | password   || size
        "login" | "email@wp.pl" | "password" || 1
        "logi1" | "emai1@wp.pl" | "passwor1" || 1
    }

    @Unroll
    def "should throw exception when user is exist"() {
        given:
        RegisterUser registerUser = prepareRegisterUser(login, email, password)
        RegisterUser userIdDB = prepareRegisterUser("login", "email@wp.pl", "password")
        authService.register(userIdDB)
        when:
        authService.register(registerUser)
        then:
        userRepository.findAll().size() == size
        thrown(AlreadyExistException)
        where:
        login   | email         | password   || size
        "login" | "difff@wp.pl" | "password" || 1
        "difff" | "email@wp.pl" | "password" || 1
    }

}
