package com.przemarcz.auth.service

import com.przemarcz.auth.model.User
import com.przemarcz.auth.model.UserRole
import com.przemarcz.auth.repository.UserRepository
import com.przemarcz.auth.repository.UserRoleRepository
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
    UserRepository userRepository
    @Autowired
    UserRoleRepository userRoleRepository
    @Autowired
    AccessConsumerService accessConsumerService

    def "should add owner role to existing user"() {
        given:
        String restaurantId = UUID.randomUUID()
        User user = userRepository.save(User.builder().build())
        AccessAvro accessAvro = new AccessAvro(AddDelete.ADD, restaurantId, user.id.toString())
        when:
        accessConsumerService.addOrDeleteOwnerRole(accessAvro)
        then:
        UserRole userRole = userRoleRepository.findAll().get(0)
        userRole.restaurantId.toString() == restaurantId
        userRole.userId.toString() == user.id.toString()
        userRepository.findAll().size() == 1
    }
}
