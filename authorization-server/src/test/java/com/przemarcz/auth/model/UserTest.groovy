package com.przemarcz.auth.model

import com.przemarcz.auth.domain.model.User
import com.przemarcz.auth.domain.model.enums.Role
import com.przemarcz.auth.exception.AlreadyExistException
import com.przemarcz.auth.exception.IllegalArgumentException
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
        user.activeAccount(user.activationKey)
        then:
        user.active
    }

    def "should not active user when user was activated before"() {
        given:
        User user = User.builder().build()
        user.generateUserActivationKey()
        user.activeAccount(user.activationKey)
        when:
        user.activeAccount(user.activationKey)
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
