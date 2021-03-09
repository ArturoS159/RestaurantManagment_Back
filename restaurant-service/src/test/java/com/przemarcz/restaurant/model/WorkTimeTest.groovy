package com.przemarcz.restaurant.model

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalTime

class WorkTimeTest extends Specification {

    @Unroll
    def "should return correct result time"() {
        given:
        WorkTime workTime = WorkTime.builder()
                .from(LocalTime.parse("07:00"))
                .to(LocalTime.parse("05:00"))
                .build()
        when:
        boolean timeInRange = workTime.isTimeInRange(LocalTime.parse(from), LocalTime.parse(to))
        then:
        timeInRange == result
        where:
        from    | to      || result
        "20:00" | "02:00" || true
        "20:00" | "22:59" || true
        "06:00" | "22:59" || false
        "06:00" | "10:00" || false
        "01:00" | "06:00" || false
        "05:30" | "06:00" || false
        "08:00" | "03:59" || true
    }
}
