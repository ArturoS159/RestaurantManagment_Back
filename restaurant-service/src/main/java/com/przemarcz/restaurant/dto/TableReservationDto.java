package com.przemarcz.restaurant.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class TableReservationDto {
    private TableReservationDto(){
    }

    @Value
    public static class CreateTablesRequest {
        List<CreateTableRequest> tables;
        int nothing;
    }

    @Builder
    @Value
    public static class CreateTableRequest {
        String name;
        Integer numberOfSeats;
        Boolean canReserve;
    }

    @Value
    public static class UpdateTablesRequest {
        List<UpdateTableRequest> tables;
        int nothing;
    }

    @Builder
    @Value
    public static class UpdateTableRequest {
        UUID id;
        String name;
        Integer numberOfSeats;
        Boolean canReserve;
    }

    @Builder
    @Value
    public static class CreateReservationRequest {
        Integer numberOfSeats;
        LocalDate day;
        LocalTime from;
        LocalTime to;
        String forename;
        String surname;
        String phoneNumber;
    }

    @Builder
    @Value
    public static class CheckReservationStatusRequest {
        Integer numberOfSeats;
        LocalDate day;
        LocalTime from;
        LocalTime to;
    }

    @Value
    public static class TablesResponse {
        List<TableResponse> tables;
    }

    @Value
    public static class TableResponse {
        UUID id;
        String name;
        Integer numberOfSeats;
        Boolean canReserve;
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
    public static class CheckReservationStatusResponse {
        boolean status;
        List<CheckReservationResponse> reservationsInThisTime;
    }

    @Value
    public static class CheckReservationResponse {
        LocalTime from;
        LocalTime to;
    }


}
