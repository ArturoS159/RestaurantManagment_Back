package com.przemarcz.auth.model

import com.przemarcz.auth.exception.AlreadyExistException
import com.przemarcz.auth.model.enums.Role
import spock.lang.Specification

class UserTest extends Specification {

    def "should add role to user"() {
        given:
        User user = User.builder().build()
        when:
        user.addRole(Role.OWNER, UUID.randomUUID())
        then:
        user.getRestaurantRoles().size() == 1
    }

    def "should active user when key is correct"() {
        given:
        User user = User.builder().build()
        user.generateUserActivationKey()
        when:
        user.activeAccount(user.userAuthorization.activationKey)
        then:
        user.active
    }

    def "should not active user when user was activated before"() {
        given:
        User user = User.builder().build()
        user.generateUserActivationKey()
        user.activeAccount(user.userAuthorization.activationKey)
        when:
        user.activeAccount(user.userAuthorization.activationKey)
        then:
        thrown AlreadyExistException
    }

    def "should not active user when key is wrong"() {
        given:
        User user = User.builder().build()
        user.generateUserActivationKey()
        when:
        user.activeAccount("badKey")
        then:
        thrown IllegalArgumentException
    }

}