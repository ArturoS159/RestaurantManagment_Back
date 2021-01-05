package com.przemarcz.restaurant.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@javax.persistence.Table(name = "tables")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(name = "restaurant_id")
    private UUID restaurantId;
    private String name;
    @Column(name = "number_of_seats")
    private Integer numberOfSeats;
    @Column(name = "is_collapse_open")
    private Boolean isCollapseOpen;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_id")
    private List<Reservation> reservations = new ArrayList<>();

    public boolean canReserveTable(LocalDate day, LocalTime from, LocalTime to) {
        List<Reservation> reservationsInDay = reservations.stream()
                .filter(reservation -> reservation.getDay().isEqual(day))
                .collect(Collectors.toList());

        List<Boolean> noReservationsAvalible = new ArrayList<>();
        reservationsInDay.forEach(reservation -> {
            if (!reservation.isReservationOpen(from, to)) {
                noReservationsAvalible.add(true);
            }
        });

        return noReservationsAvalible.isEmpty();
    }
}
