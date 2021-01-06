package com.przemarcz.restaurant.model

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalTime

class ReservationTest extends Specification {

    @Unroll
    def "should check reservation is open"() {
        given:
        Reservation reservation = Reservation.builder()
                .from(LocalTime.parse("15:00"))
                .to(LocalTime.parse("16:00"))
                .build()
        when:
        boolean reservationOpen = reservation.isReservationOpen(LocalTime.parse(timeFrom), LocalTime.parse(timeTo))
        then:
        reservationOpen == open
        where:
        timeFrom | timeTo  | open
        "13:00"  | "15:00" | false
        "13:00"  | "16:00" | false
        "15:00"  | "17:00" | false
        "16:00"  | "17:00" | false
        "15:00"  | "16:00" | false
        "14:59"  | "16:00" | false
        "15:00"  | "16:01" | false
        "15:00"  | "16:00" | false
        "15:30"  | "16:00" | false
        "15:00"  | "15:30" | false
        "15:30"  | "17:00" | false
        "13:00"  | "15:30" | false
        "10:00"  | "20:00" | false
        "10:00"  | "14:59" | true
        "16:01"  | "17:00" | true
        "10:01"  | "12:00" | true
        "17:01"  | "20:00" | true
    }

}
