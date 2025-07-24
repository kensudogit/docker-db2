package com.aircargo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CARGO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cargo {

    @Id
    @Column(name = "CARGO_ID")
    private String cargoId;

    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;

    @Column(name = "ORIGIN_AIRPORT")
    private String originAirport;

    @Column(name = "DESTINATION_AIRPORT")
    private String destinationAirport;

    @Column(name = "CARGO_TYPE")
    private String cargoType;

    @Column(name = "WEIGHT")
    private BigDecimal weight;

    @Column(name = "VOLUME")
    private BigDecimal volume;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "SHIPPER_NAME")
    private String shipperName;

    @Column(name = "CONSIGNEE_NAME")
    private String consigneeName;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    @Version
    @Column(name = "VERSION")
    private Integer version;
} 