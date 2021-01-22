package com.przemarcz.restaurant.dto;

import com.przemarcz.restaurant.model.Table;
import lombok.Builder;
import lombok.Value;
import org.codehaus.jackson.annotate.JsonIgnore;

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
        String tableName;
        Integer sizeOfTable;
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
        String tableName;
        Integer sizeOfTable;
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
        List<CheckReservationResponse> reservations;
        @JsonIgnore
        List<Table> tables;
    }

    @Value
    public static class CheckReservationResponse {
        UUID tableId;
        List<Time> times;
    }

    @Value
    public static class Time {
        LocalTime from;
        LocalTime to;
    }


}
