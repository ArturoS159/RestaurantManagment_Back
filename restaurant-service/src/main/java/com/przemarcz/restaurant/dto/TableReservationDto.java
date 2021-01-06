package com.przemarcz.restaurant.dto;

import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
    public static class UpdateTableRequest {
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
    public static class CheckReservationStatusRequest {
        Integer numberOfSeats;
        LocalDate day;
        LocalTime from;
        LocalTime to;
    }

    @Value
    public static class TableResponse {
        UUID id;
        String name;
        Integer numberOfSeats;
        Boolean isCollapseOpen;
    }

    @Value
    public static class ReservationResponse {
        LocalDate day;
        LocalTime from;
        LocalTime to;
        String forename;
        String surname;
        String phoneNumber;
    }

    @Value
    public static class MyReservationResponse {
        String restaurantName;
        LocalDate day;
        LocalTime from;
        LocalTime to;
        String forename;
        String surname;
        String phoneNumber;
    }

    @Value
    public static class CheckReservationResponse {
        LocalTime from;
        LocalTime to;
    }

    @Value
    public static class CheckReservationStatusResponse {
        boolean status;
        List<CheckReservationResponse> reservationsInThisTime;
    }


}
