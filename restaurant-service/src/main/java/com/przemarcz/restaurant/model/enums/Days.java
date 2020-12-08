package com.przemarcz.restaurant.model.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum Days {

    MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

    private final int num;

    public static Optional<Days> valueOf(int i) {
        return Arrays.stream(values())
                .filter(day -> day.num==i)
                .findFirst();
    }
}
