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
@Table(name = "OUTBOUND")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Outbound {

    @Id
    @Column(name = "OUTBOUND_ID", length = 20)
    @NotBlank(message = "出荷IDは必須です")
    private String outboundId;

    @Column(name = "CARGO_ID", length = 20, nullable = false)
    @NotBlank(message = "貨物IDは必須です")
    private String cargoId;

    @Column(name = "FLIGHT_NUMBER", length = 10, nullable = false)
    @NotBlank(message = "フライト番号は必須です")
    private String flightNumber;

    @Column(name = "DEPARTURE_DATE", nullable = false)
    @NotNull(message = "出発日は必須です")
    private LocalDate departureDate;

    @Column(name = "DEPARTURE_TIME", nullable = false)
    @NotNull(message = "出発時刻は必須です")
    private LocalTime departureTime;

    @Column(name = "TERMINAL", length = 10, nullable = false)
    @NotBlank(message = "ターミナルは必須です")
    private String terminal;

    @Column(name = "STATUS", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private OutboundStatus status = OutboundStatus.SCHEDULED;

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

    public enum OutboundStatus {
        SCHEDULED("予定済み"),
        READY("準備完了"),
        LOADING("積込中"),
        DEPARTED("出発済み"),
        IN_TRANSIT("輸送中");

        private final String displayName;

        OutboundStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
} 