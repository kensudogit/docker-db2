package com.aircargo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "INBOUND")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Inbound {

    @Id
    @Column(name = "INBOUND_ID", length = 20)
    @NotBlank(message = "入荷IDは必須です")
    private String inboundId;

    @Column(name = "CARGO_ID", length = 20, nullable = false)
    @NotBlank(message = "貨物IDは必須です")
    private String cargoId;

    @Column(name = "FLIGHT_NUMBER", length = 10, nullable = false)
    @NotBlank(message = "フライト番号は必須です")
    private String flightNumber;

    @Column(name = "ARRIVAL_DATE", nullable = false)
    @NotNull(message = "到着日は必須です")
    private LocalDate arrivalDate;

    @Column(name = "ARRIVAL_TIME", nullable = false)
    @NotNull(message = "到着時刻は必須です")
    private LocalTime arrivalTime;

    @Column(name = "TERMINAL", length = 10, nullable = false)
    @NotBlank(message = "ターミナルは必須です")
    private String terminal;

    @Column(name = "STATUS", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private InboundStatus status = InboundStatus.ARRIVED;

    @Column(name = "HANDLER_ID", length = 20)
    private String handlerId;

    @Column(name = "NOTES", length = 1000)
    private String notes;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARGO_ID", insertable = false, updatable = false)
    private Cargo cargo;

    public enum InboundStatus {
        ARRIVED("到着済み"),
        UNLOADING("荷下ろし中"),
        INSPECTION("検査中"),
        READY("準備完了"),
        DELIVERED("配達済み");

        private final String displayName;

        InboundStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
} 