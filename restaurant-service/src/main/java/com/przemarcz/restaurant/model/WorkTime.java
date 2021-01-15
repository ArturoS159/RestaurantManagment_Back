package com.przemarcz.restaurant.model;

import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.model.enums.Days;
import lombok.*;

import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalTime;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "work_time")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class WorkTime {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(name = "restaurant_id")
    private UUID restaurantId;
    @Enumerated(EnumType.STRING)
    @Column(name = "work_day")
    private Days day;
    @Column(name = "from_time")
    private LocalTime from;
    @Column(name = "to_time")
    private LocalTime to;

    public boolean isDayEquals(int day) {
        return this.day.equals(Days.valueOf(day).orElseThrow(() -> new NotFoundException("Not found!")));
    }

    public boolean isTimeInRange(LocalTime from, LocalTime to) {
        return (nonNull(this.from) && nonNull(this.to)) && (from.isAfter(this.from) || to.isBefore(this.to));
    }
}
