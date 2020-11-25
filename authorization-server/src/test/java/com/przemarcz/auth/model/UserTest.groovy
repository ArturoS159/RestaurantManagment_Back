package com.przemarcz.auth.model


import com.przemarcz.auth.exception.AlreadyExistException
import com.przemarcz.auth.exception.NotFoundException
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

    def "should remove role from User"() {
        given:
        User user = prepareUser("login", "email@wp.pl", "password")
        UUID restaurant1 = UUID.randomUUID()
        UUID restaurant2 = UUID.randomUUID()
        UUID restaurant3 = UUID.randomUUID()
        user.addRole(Role.OWNER,restaurant1)
        user.addRole(Role.OWNER,restaurant2)
        user.addRole(Role.WORKER,restaurant3)
        when:
        user.delRole(Role.OWNER,restaurant1)
        then:
        user.getRestaurantRoles().size()==2
        user.getRestaurantRoles().get(0).getRole()==Role.OWNER
        user.getRestaurantRoles().get(0).getRestaurantId()==restaurant2
        user.getRestaurantRoles().get(1).getRole()==Role.WORKER
        user.getRestaurantRoles().get(1).getRestaurantId()==restaurant3
    }

    def "should throw exception role in user not found"() {
        given:
        User user = prepareUser("login", "email@wp.pl", "password")
        UUID restaurant1 = UUID.randomUUID()
        user.addRole(Role.OWNER,restaurant1)
        when:
        user.delRole(Role.OWNER,UUID.randomUUID())
        then:
        user.getRestaurantRoles().size()==1
        thrown(NotFoundException)
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

    def "should throw exception user is activated"(){
        given:
        User user = prepareUser("login", "email@wp.pl", "password")
        user.generateUserActivationKey()
        user.active=true
        when:
        user.activeAccount(user.getUserAuthorization().getActivationKey())
        then:
        thrown(AlreadyExistException)
    }

    def "should throw exception bad activation key"(){
        given:
        User user = prepareUser("login", "email@wp.pl", "password")
        user.generateUserActivationKey()
        when:
        user.activeAccount("badKey")
        then:
        thrown(IllegalArgumentException)
    }

}
