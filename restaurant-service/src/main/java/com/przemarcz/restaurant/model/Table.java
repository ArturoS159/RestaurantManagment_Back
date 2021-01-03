package com.przemarcz.restaurant.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@javax.persistence.Table(name = "work_time")
@Getter
@Setter
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

    public boolean isNumberEqualsSeats(Integer numberOfSeats){
        return this.numberOfSeats.equals(numberOfSeats);
    }
}
