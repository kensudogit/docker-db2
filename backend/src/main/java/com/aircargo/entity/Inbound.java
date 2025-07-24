package com.aircargo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "INBOUND")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inbound {

    @Id
    @Column(name = "INBOUND_ID")
    private String inboundId;

    @Column(name = "CARGO_ID")
    private String cargoId;

    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;

    @Column(name = "ARRIVAL_DATE")
    private LocalDate arrivalDate;

    @Column(name = "ARRIVAL_TIME")
    private LocalTime arrivalTime;

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