package com.przemarcz.restaurant.model;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Column(name = "can_reserve")
    private Boolean canReserve;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_id")
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
}
