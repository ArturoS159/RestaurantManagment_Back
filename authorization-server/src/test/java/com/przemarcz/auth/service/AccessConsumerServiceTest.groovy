package com.przemarcz.auth.service

import com.przemarcz.auth.exception.NotFoundException
import com.przemarcz.auth.model.User
import com.przemarcz.auth.repository.UserRepository
import com.przemarcz.avro.AccessAvro
import com.przemarcz.avro.AddDelete
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:bootstrap-test.yml")
class AccessConsumerServiceTest extends Specification {

    @Autowired
    AccessConsumerService accessConsumerService
    @Autowired
    UserRepository userRepository

    def setup() {
        userRepository.deleteAll()
    }

    AccessAvro prepareAccessAvro(String userId, String restaurantId, AddDelete type){
        AccessAvro accessAvro = new AccessAvro()
        accessAvro.restaurantId = restaurantId
        accessAvro.userId = userId
        accessAvro.type = type
        return accessAvro
    }

    User prepareUser(String login, String email) {
        User user = new User()
        user.email = email
        user.password = "password"
        user.login = login
        return user
    }

    def "should add owner role to user"(){
        given:
        User user = prepareUser("login", "email@wp.pl")
        String restaurantId = UUID.randomUUID().toString()
        userRepository.save(user)
        AccessAvro accessAvro = prepareAccessAvro(user.id.toString(), restaurantId, AddDelete.ADD)
        when:
        accessConsumerService.addOrDeleteOwnerRole(accessAvro)
        then:
        userRepository.findById(user.id).get().getRestaurantRoles().size()==1
        userRepository.findById(user.id).get().getRestaurantRoles().get(0).getRestaurantId().toString()==restaurantId
    }

    def "should throw exception when try to remove not exist user"(){
        given:
        User user = prepareUser("login", "email@wp.pl")
        String restaurantId = UUID.randomUUID().toString()
        userRepository.save(user)
        AccessAvro accessAvro = prepareAccessAvro(user.id.toString(), restaurantId, AddDelete.ADD)
        AccessAvro accessAvro1 = prepareAccessAvro(UUID.randomUUID().toString(), restaurantId, AddDelete.DEL)
        when:
        accessConsumerService.addOrDeleteOwnerRole(accessAvro)
        accessConsumerService.addOrDeleteOwnerRole(accessAvro1)
        then:
        userRepository.findById(user.id).get().getRestaurantRoles().size()==1
        thrown(NotFoundException)
    }

    def "should delete access"(){
        given:
        User user = prepareUser("login", "email@wp.pl")
        String restaurantId = UUID.randomUUID().toString()
        userRepository.save(user)
        AccessAvro accessAvro = prepareAccessAvro(user.id.toString(), restaurantId, AddDelete.ADD)
        AccessAvro accessAvro1 = prepareAccessAvro(user.id.toString(), restaurantId, AddDelete.DEL)
        when:
        accessConsumerService.addOrDeleteOwnerRole(accessAvro)
        accessConsumerService.addOrDeleteOwnerRole(accessAvro1)
        then:
        userRepository.findById(user.id).get().getRestaurantRoles().size()==0
    }
}
