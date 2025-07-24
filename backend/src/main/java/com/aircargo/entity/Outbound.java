package com.aircargo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "OUTBOUND")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Outbound {

    @Id
    @Column(name = "OUTBOUND_ID")
    private String outboundId;

    @Column(name = "CARGO_ID")
    private String cargoId;

    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;

    @Column(name = "DEPARTURE_DATE")
    private LocalDate departureDate;

    @Column(name = "DEPARTURE_TIME")
    private LocalTime departureTime;

    @Column(name = "TERMINAL")
    private String terminal;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "HANDLER_ID")
    private String handlerId;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Version
    @Column(name = "VERSION")
    private Integer version;
} 