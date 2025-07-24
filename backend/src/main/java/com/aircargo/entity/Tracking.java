package com.aircargo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRACKING")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tracking {

    @Id
    @Column(name = "TRACKING_ID")
    private String trackingId;

    @Column(name = "CARGO_ID")
    private String cargoId;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    @Column(name = "HANDLER_ID")
    private String handlerId;

    @Column(name = "NOTES")
    private String notes;

    @Version
    @Column(name = "VERSION")
    private Integer version;
} 