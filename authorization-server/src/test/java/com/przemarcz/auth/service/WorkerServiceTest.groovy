package com.przemarcz.auth.service

import com.przemarcz.auth.exception.AlreadyExistException
import com.przemarcz.auth.exception.NotFoundException
import com.przemarcz.auth.model.User
import com.przemarcz.auth.model.UserRole
import com.przemarcz.auth.model.enums.Role
import com.przemarcz.auth.repository.UserRepository
import com.przemarcz.auth.repository.UserRoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static com.przemarcz.auth.dto.UserDto.WorkerResponse
import static org.springframework.data.domain.Pageable.unpaged

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:bootstrap-test.yml")
class WorkerServiceTest extends Specification {

    @Autowired
    UserRepository userRepository
    @Autowired
    UserRoleRepository userRoleRepository
    @Autowired
    WorkerService workerService

    def setup() {
        userRepository.deleteAll()
        userRoleRepository.deleteAll()
    }

    def "should get all workers from only one restaurnat"() {
        given:
        UUID restaurant1 = UUID.randomUUID()
        UUID restaurant2 = UUID.randomUUID()
        User worker1 = User.builder()
                .id(UUID.randomUUID())
                .email("worker1@test.pl")
                .build()
        User worker2 = User.builder()
                .id(UUID.randomUUID())
                .email("worker2@test.pl")
                .build()
        User worker3 = User.builder()
                .id(UUID.randomUUID())
                .email("worker3@test.pl")
                .build()
        worker1.addRole(Role.WORKER, restaurant1)
        worker2.addRole(Role.WORKER, restaurant1)
        worker3.addRole(Role.WORKER, restaurant2)
        userRepository.saveAll(Arrays.asList(worker1, worker2, worker3))
        when:
        Page<WorkerResponse> workers1 = workerService.getAllRestaurantWorkers(restaurant1, unpaged())
        Page<WorkerResponse> workers2 = workerService.getAllRestaurantWorkers(restaurant2, unpaged())
        then:
        workers1.size() == 2
        workers2.size() == 1
        userRoleRepository.findAll().size() == 3
    }

    def "should add worker to restaurant when worker found"() {
        given:
        UUID restaurantId = UUID.randomUUID()
        User worker = User.builder()
                .id(UUID.randomUUID())
                .email("test@test.pl")
                .build()
        userRepository.save(worker)
        when:
        workerService.addRestaurantWorker(restaurantId, "test@test.pl")
        then:
        UserRole workerRole = userRoleRepository.findAll().get(0)
        workerRole.userId == worker.id
        workerRole.restaurantId == restaurantId
        userRoleRepository.findAll().size() == 1
    }

    def "should not add worker to restaurant when worker not found"() {
        given:
        UUID restaurantId = UUID.randomUUID()
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@test.pl")
                .build()
        userRepository.save(user)
        when:
        workerService.addRestaurantWorker(restaurantId, "notfound@test.pl")
        then:
        thrown NotFoundException
        userRoleRepository.findAll().size() == 0
    }

    def "should not add worker to restaurant when worker was aded before"() {
        given:
        UUID restaurantId = UUID.randomUUID()
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .email("test@test.pl")
                .build()
        User user2 = User.builder()
                .id(UUID.randomUUID())
                .email("test2@test.pl")
                .build()
        userRepository.saveAll(Arrays.asList(user1, user2))
        workerService.addRestaurantWorker(restaurantId, "test@test.pl")
        when:
        workerService.addRestaurantWorker(restaurantId, "test@test.pl")
        then:
        thrown AlreadyExistException
        userRoleRepository.findAll().size() == 1
    }

    def "should delete worker from restaurant"() {
        given:
        UUID restaurantId = UUID.randomUUID()
        User worker1 = User.builder()
                .id(UUID.randomUUID())
                .email("test@test.pl")
                .build()
        User worker2 = User.builder()
                .id(UUID.randomUUID())
                .email("test2@test.pl")
                .build()
        userRepository.saveAll(Arrays.asList(worker1, worker2))
        workerService.addRestaurantWorker(restaurantId, "test@test.pl")
        workerService.addRestaurantWorker(restaurantId, "test2@test.pl")
        when:
        workerService.deleteRestaurantWorker(restaurantId, worker1.id)
        then:
        userRoleRepository.findAll().size() == 1
    }
}
