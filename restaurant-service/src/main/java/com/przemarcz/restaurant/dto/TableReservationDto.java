package com.przemarcz.restaurant.dto;

import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class TableReservationDto {
    private TableReservationDto(){
    }

    @Value
    public static class CreateTableRequest {
        String name;
        Integer numberOfSeats;
        Boolean isCollapseOpen;
    }

    @Value
    public static class AddReservationRequest {
        Integer numberOfSeats;
        LocalDate day;
        LocalTime from;
        LocalTime to;
        String forename;
        String surname;
        String phoneNumber;
    }

    @Value
    public static class TableResponse {
        UUID id;
        String name;
        Integer numberOfSeats;
        Boolean isCollapseOpen;
    }

    @Value
    public static class ReservationTableResponse {

    }

}
