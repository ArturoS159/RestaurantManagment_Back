package com.przemarcz.auth.model

import com.przemarcz.auth.exception.AlreadyExistException
import com.przemarcz.auth.model.enums.Role
import spock.lang.Specification

class UserTest extends Specification {

    User prepareUser(String login, String email, String password){
        User user = new User()
        user.login=login
        user.email=email
        user.password=password
        return user
    }

    def "should add role to User"() {
        given:
        User user = prepareUser("login", "email@wp.pl", "password")
        when:
        user.addRole(Role.OWNER,UUID.randomUUID())
        then:
        user.getRestaurantRoles().size()==1
    }

    def "should active user account"(){
        given:
        User user = prepareUser("login", "email@wp.pl", "password")
        user.generateUserActivationKey()
        when:
        user.activeAccount(user.getUserAuthorization().getActivationKey())
        then:
        user.active
    }

    def "should throw exception when user is activated before"(){
        given:
        User user = prepareUser("login", "email@wp.pl", "password")
        user.generateUserActivationKey()
        user.active=true
        when:
        user.activeAccount(user.getUserAuthorization().getActivationKey())
        then:
        thrown(AlreadyExistException)
    }

    def "should throw exception when bad activation key"(){
        given:
        User user = prepareUser("login", "email@wp.pl", "password")
        user.generateUserActivationKey()
        when:
        user.activeAccount("badKey")
        then:
        thrown(IllegalArgumentException)
    }

}
