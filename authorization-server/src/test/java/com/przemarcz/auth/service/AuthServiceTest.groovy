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

    User prepareUser(String login, String email) {
        User user = new User()
        user.email = email
        user.password = "password"
        user.login = login
        return user
    }

    UserRegisterRequest prepareRegisterUser(String login, String email, String password) {
        return new UserRegisterRequest(login,email,password)
    }

    @Unroll
    def "should create User"() {
        given:
        UserRegisterRequest registerUser = new UserRegisterRequest(login,email,password)
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
        UserRegisterRequest registerUser = prepareRegisterUser(login, email, password)
        UserRegisterRequest userIdDB = new UserRegisterRequest("login", "email@wp.pl", "password")
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

    def "should active user account"() {
        given:
        User user = prepareUser("login", "email@wp.pl")
        user.generateUserActivationKey()
        userRepository.save(user)
        UserActivationRequest userActivation = new UserActivationRequest("login", user.getUserAuthorization().getActivationKey())
        when:
        authService.active(userActivation)
        then:
        userRepository.findById(user.getId()).get().active
    }

    def "should return correct User"() {
        given:
        User user = prepareUser("login", "email@wp.pl")
        User user1 = prepareUser("login1", "email1@wp.pl")
        userRepository.save(user)
        userRepository.save(user1)
        when:
        UserResponse userFromDb = authService.getUser(user.getId().toString())
        then:
        userFromDb.login=="login"
        userFromDb.email=="email@wp.pl"
        userRepository.findAll().size()==2
    }

    def "should correct login when user try to login by login"(){
        given:
        User user = prepareUser("login", "email@wp.pl")
        User user1 = prepareUser("login1", "email1@wp.pl")
        userRepository.save(user)
        userRepository.save(user1)
        when:
        UserDetails userDetails = authService.loadUserByUsername("login")
        then:
        userDetails.getUsername()==user.id.toString()
    }

    def "should correct login when user try to login by userId"(){
        given:
        User user = prepareUser("login", "email@wp.pl")
        User user1 = prepareUser("login1", "email1@wp.pl")
        userRepository.save(user)
        userRepository.save(user1)
        when:
        UserDetails userDetails = authService.loadUserByUsername(user.id.toString())
        then:
        userDetails.getUsername()==user.id.toString()
    }

    def "should throw exception user not found by login"(){
        given:
        User user = prepareUser("login", "email@wp.pl")
        User user1 = prepareUser("login1", "email1@wp.pl")
        userRepository.save(user)
        userRepository.save(user1)
        when:
        UserDetails userDetails = authService.loadUserByUsername("emptyLogin")
        then:
        userDetails==null
        thrown(NotFoundException)
    }

    def "should throw exception user not found by id"(){
        given:
        User user = prepareUser("login", "email@wp.pl")
        User user1 = prepareUser("login1", "email1@wp.pl")
        userRepository.save(user)
        userRepository.save(user1)
        when:
        UserDetails userDetails = authService.loadUserByUsername(UUID.randomUUID().toString())
        then:
        userDetails==null
        thrown(NotFoundException)
    }

    def "should update User"(){
        given:
        User user = prepareUser("login", "email@wp.pl")
        userRepository.save(user)
        UserUpdateRequest userDto = new UserUpdateRequest(null,null,null,"cityNew",null,null,null)
        when:
        authService.updateUser(userDto, user.id.toString())
        then:
        userRepository.findById(user.id).get().login=="login"
        userRepository.findById(user.id).get().email=="email@wp.pl"
        userRepository.findById(user.id).get().city=="cityNew"
    }
}
