package com.przemarcz.restaurant.service


import com.przemarcz.restaurant.repository.RestaurantRepository
import com.przemarcz.restaurant.repository.TableRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:bootstrap-test.yml")
class TableServiceTest extends Specification {

    @Autowired
    TableService tableService
    @Autowired
    RestaurantRepository restaurantRepository
    @Autowired
    TableRepository tableRepository

    def setup() {
        restaurantRepository.deleteAll()
    }

//    def "should add table to restaurnat"() {
//        given:
//        UUID restaurantId = UUID.randomUUID()
//        Restaurant restaurant = Restaurant.builder()
//                .id(restaurantId)
//                .build()
//        CreateUpdateTableRequest table = CreateUpdateTableRequest.builder()
//                .name("Bar1")
//                .canReserve(true)
//                .numberOfSeats(3)
//                .build()
//        restaurantRepository.save(restaurant)
//        when:
//        tableService.addTables(restaurantId, table)
//        then:
//        tableRepository.findAll().size() == 1
//    }
//
//    def "should add table when restaurant not found"() {
//        given:
//        UUID restaurantId = UUID.randomUUID()
//        Restaurant restaurant = Restaurant.builder()
//                .id(restaurantId)
//                .build()
//        CreateUpdateTableRequest table = CreateUpdateTableRequest.builder()
//                .name("Bar1")
//                .canReserve(true)
//                .numberOfSeats(3)
//                .build()
//        restaurantRepository.save(restaurant)
//        when:
//        tableService.addTables(UUID.randomUUID(), table)
//        then:
//        thrown NotFoundException
//        tableRepository.findAll().size() == 0
//    }
//
////    def "should update table in restaurant"() {
////        given:
////        UUID restaurantId = UUID.randomUUID()
////        UUID tableId = UUID.randomUUID()
////        Restaurant restaurant = Restaurant.builder()
////                .id(restaurantId)
////                .build()
////        Table tableInDb = Table.builder()
////                .id(tableId)
////                .restaurantId(restaurantId)
////                .name("Bar1")
////                .canReserve(true)
////                .numberOfSeats(2)
////                .build()
////        UpdateTableRequest updateRequest = UpdateTableRequest.builder()
////                .name("Bar2")
////                .canReserve(false)
////                .numberOfSeats(5)
////                .build()
////        restaurantRepository.save(restaurant)
////        tableRepository.save(tableInDb)
////        when:
////        tableService.updateTable(restaurantId, tableId, updateRequest)
////        then:
////        Table tableResponse = tableRepository.findAll().get(0)
////        tableResponse.name == "Bar2"
////        !tableResponse.canReserve
////        tableResponse.numberOfSeats == 5
////    }
//
//    def "should delete table from restaurant"() {
//        given:
//        UUID restaurantId = UUID.randomUUID()
//        UUID tableId = UUID.randomUUID()
//        Restaurant restaurant = Restaurant.builder()
//                .id(restaurantId)
//                .build()
//        Table tableInDb1 = Table.builder()
//                .id(tableId)
//                .restaurantId(restaurantId)
//                .name("Bar1")
//                .canReserve(true)
//                .numberOfSeats(2)
//                .build()
//        Table tableInDb2 = Table.builder()
//                .id(UUID.randomUUID())
//                .restaurantId(restaurantId)
//                .name("Bar2")
//                .canReserve(false)
//                .numberOfSeats(3)
//                .build()
//        restaurantRepository.save(restaurant)
//        tableRepository.saveAll(Arrays.asList(tableInDb1, tableInDb2))
//        when:
//        tableService.deleteTable(restaurantId, tableId)
//        then:
//        tableRepository.findAll().size()==1
//        tableRepository.findAll().get(0).name=="Bar2"
//    }
}
