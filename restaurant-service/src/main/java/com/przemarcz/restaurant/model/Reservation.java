package com.przemarcz.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@javax.persistence.Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(name = "table_id")
    private UUID tableId;
    private LocalDate day;
    private LocalTime from;
    private LocalTime to;
    private String forename;
    private String surname;
    private String phoneNumber;
}
