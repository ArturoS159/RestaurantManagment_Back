package com.przemarcz.restaurant.model;

import com.przemarcz.restaurant.model.enums.Days;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "work_time")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkTime {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(name = "restaurant_id")
    private UUID restaurantId;
    @Enumerated(EnumType.STRING)
    @Column(name = "work_day")
    private Days day;
    @Column(name = "from_time")
    private LocalTime from;
    @Column(name = "to_time")
    private LocalTime to;

    public WorkTime(Days day, LocalTime from, LocalTime to) {
        this.day = day;
        this.from = from;
        this.to = to;
    }
}
