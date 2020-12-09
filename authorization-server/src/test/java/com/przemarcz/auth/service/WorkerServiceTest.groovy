package com.przemarcz.auth.service

import com.przemarcz.auth.exception.AlreadyExistException
import com.przemarcz.auth.exception.NotFoundException
import com.przemarcz.auth.model.User
import com.przemarcz.auth.repository.UserRepository
import com.przemarcz.auth.repository.UserRoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:bootstrap-test.yml")
class WorkerServiceTest extends Specification {

    @Autowired
    WorkerService workerService
    @Autowired
    UserRoleRepository userRoleRepository
    @Autowired
    UserRepository userRepository

    def setup() {
        userRepository.deleteAll()
        userRoleRepository.deleteAll()
    }

    User prepareUser(String email, String login) {
        User user = new User()
        user.email = email
        user.password = "password"
        user.login = login
        return user
    }

    @Unroll
    def "should add restaurant worker"() {
        given:
        userRepository.save(prepareUser("test@wp.pl","login"))
        when:
        workerService.addRestaurantWorker(UUID.randomUUID(), email)
        then:
        userRoleRepository.findAll().size() == size
        where:
        email        || size
        "test@wp.pl" || 1
        "test@Wp.pL" || 1
        "TEsT@wp.pL" || 1
    }

    def "should add restaurant worker when is different restaurant"() {
        given:
        userRepository.save(prepareUser("test@wp.pl", "login"))
        when:
        workerService.addRestaurantWorker(UUID.randomUUID(), "test@wp.pl")
        workerService.addRestaurantWorker(UUID.randomUUID(), "test@wp.pl")
        then:
        userRoleRepository.findAll().size() == 2
    }

    def "should not add restaurant worker when is added before"() {
        given:
        userRepository.save(prepareUser("test@wp.pl", "login"))
        UUID uuid = UUID.randomUUID()
        when:
        workerService.addRestaurantWorker(uuid, "test@wp.pl")
        workerService.addRestaurantWorker(uuid, "test@wp.pl")
        then:
        userRoleRepository.findAll().size() == 1
        thrown(AlreadyExistException)
    }

    def "should throw exception when try to add not exist user to worker"() {
        given:
        userRepository.save(prepareUser("test@wp.pl", "login"))
        when:
        workerService.addRestaurantWorker(UUID.randomUUID(), "notexist@wp.pl")
        then:
        userRoleRepository.findAll().size() == 0
        userRepository.findAll().size()==1
        thrown(NotFoundException)
    }

    def "should get all workers from only one restaurant"() {
        given:
        userRepository.save(prepareUser("test1@wp.pl", "login1"))
        userRepository.save(prepareUser("test2@wp.pl", "login2"))
        UUID restaurantId = UUID.randomUUID()
        UUID restaurantIdDiff = UUID.randomUUID()
        workerService.addRestaurantWorker(restaurantId, "test1@wp.pl")
        workerService.addRestaurantWorker(restaurantId, "test2@wp.pl")
        workerService.addRestaurantWorker(restaurantIdDiff, "test2@wp.pl")
        when:
        def sizeRestaurantFirst = workerService.getAllRestaurantWorkers(restaurantId, Pageable.unpaged()).size
        def sizeRestaurantSec = workerService.getAllRestaurantWorkers(restaurantIdDiff, Pageable.unpaged()).size
        then:
        userRoleRepository.findAll().size() == 3
        sizeRestaurantFirst == 2
        sizeRestaurantSec == 1
    }

    def "should delete worker from restaurant"() {
        given:
        User user = userRepository.save(prepareUser("test@wp.pl", "login"))
        UUID restaurantId = UUID.randomUUID()
        workerService.addRestaurantWorker(restaurantId, "test@wp.pl")
        workerService.addRestaurantWorker(UUID.randomUUID(), "test@wp.pl")
        when:
        workerService.deleteRestaurantWorker(restaurantId, user.getId())
        then:
        userRepository.findById(user.getId()).get().restaurantRoles.size()==1
    }

    def "should throw exception when try to delete not existing worker"() {
        given:
        userRepository.save(prepareUser("test@wp.pl", "login"))
        when:
        workerService.deleteRestaurantWorker(UUID.randomUUID(), UUID.randomUUID())
        then:
        thrown(NotFoundException)
    }

    def "should throw exception when try to delete not existing role in user"() {
        given:
        User user = userRepository.save(prepareUser("test@wp.pl", "login"))
        UUID restaurantId = UUID.randomUUID()
        workerService.addRestaurantWorker(restaurantId, "test@wp.pl")
        when:
        workerService.deleteRestaurantWorker(UUID.randomUUID(), user.getId())
        then:
        userRepository.findById(user.getId()).get().restaurantRoles.size()==1
        thrown(NotFoundException)
    }
}