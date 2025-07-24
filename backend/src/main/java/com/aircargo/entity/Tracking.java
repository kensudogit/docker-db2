package com.aircargo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRACKING")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Tracking {

    @Id
    @Column(name = "TRACKING_ID", length = 20)
    @NotBlank(message = "追跡IDは必須です")
    private String trackingId;

    @Column(name = "CARGO_ID", length = 20, nullable = false)
    @NotBlank(message = "貨物IDは必須です")
    private String cargoId;

    @Column(name = "LOCATION", length = 100, nullable = false)
    @NotBlank(message = "場所は必須です")
    private String location;

    @Column(name = "STATUS", length = 50, nullable = false)
    @NotBlank(message = "ステータスは必須です")
    private String status;

    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    @Column(name = "HANDLER_ID", length = 20)
    private String handlerId;

    @Column(name = "NOTES", length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARGO_ID", insertable = false, updatable = false)
    private Cargo cargo;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
} 