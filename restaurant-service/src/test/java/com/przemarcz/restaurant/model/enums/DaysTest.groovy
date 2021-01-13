package com.przemarcz.restaurant.model.enums

import spock.lang.Specification

class DaysTest extends Specification {

    def"should return correct day"(){
        expect:
        Days.valueOf(5).get() == Days.FRIDAY
    }
}
