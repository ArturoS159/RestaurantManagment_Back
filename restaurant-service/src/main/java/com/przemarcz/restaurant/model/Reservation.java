package com.przemarcz.restaurant.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@javax.persistence.Table(name = "reservations")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class Reservation {
    @Id
    private UUID id;
    @Column(name = "table_id")
    private UUID tableId;
    @Column(name = "restaurant_id")
    private UUID restaurantId;
    @Column(name = "restaurant_name")
    private String restaurantName;
    @Column(name = "size_of_table")
    private Integer sizeOfTable;
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "reservation_day")
    private LocalDate day;
    @Column(name = "from_time")
    private LocalTime from;
    @Column(name = "to_time")
    private LocalTime to;
    private String forename;
    private String surname;
    @Column(name = "phone_number")
    private String phoneNumber;

    public boolean isReservationOpen(LocalTime from, LocalTime to) {
        boolean left = (from.isBefore(this.from) || from.equals(this.from)) && (to.isAfter(this.from) || to.equals(this.from));
        boolean right = (from.isBefore(this.to) || from.equals(this.to)) && (to.isAfter(this.to) || to.equals(this.to));
        boolean inside = from.isAfter(this.from) && to.isBefore(this.to);
        boolean outside = from.isBefore(this.from) && to.isAfter(this.to);
        boolean equals = from.equals(this.from) && to.equals(this.to);
        return !(left || right || inside || outside || equals);
    }
}
