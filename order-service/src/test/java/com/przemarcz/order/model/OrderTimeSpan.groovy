package com.przemarcz.order.model

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OrderTimeSpan extends Specification {

    @Unroll
    def "should return is order expired"() {
        given:
        Order order = Order.builder()
                .time(LocalDateTime.parse("2020-01-01 10:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build()
        when:
        order.isOrderExpired(LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
        then:
        true
        where:
        time               || expectValue
        "2020-01-01 10:30" || false
        "2020-01-01 09:10" || false
        "2020-01-01 11:30" || false
        "2020-01-01 11:31" || true
        "2020-01-02 10:00" || true
    }
}
