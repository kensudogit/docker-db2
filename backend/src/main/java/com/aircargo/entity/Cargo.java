package com.aircargo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CARGO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Cargo {

    @Id
    @Column(name = "CARGO_ID", length = 20)
    @NotBlank(message = "貨物IDは必須です")
    private String cargoId;

    @Column(name = "FLIGHT_NUMBER", length = 10, nullable = false)
    @NotBlank(message = "フライト番号は必須です")
    private String flightNumber;

    @Column(name = "ORIGIN_AIRPORT", length = 3, nullable = false)
    @NotBlank(message = "出発空港は必須です")
    private String originAirport;

    @Column(name = "DESTINATION_AIRPORT", length = 3, nullable = false)
    @NotBlank(message = "到着空港は必須です")
    private String destinationAirport;

    @Column(name = "CARGO_TYPE", length = 50, nullable = false)
    @NotBlank(message = "貨物タイプは必須です")
    private String cargoType;

    @Column(name = "WEIGHT", precision = 10, scale = 2, nullable = false)
    @NotNull(message = "重量は必須です")
    @Positive(message = "重量は正の数である必要があります")
    private BigDecimal weight;

    @Column(name = "VOLUME", precision = 10, scale = 2, nullable = false)
    @NotNull(message = "容積は必須です")
    @Positive(message = "容積は正の数である必要があります")
    private BigDecimal volume;

    @Column(name = "STATUS", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private CargoStatus status = CargoStatus.PENDING;

    @Column(name = "SHIPPER_NAME", length = 100, nullable = false)
    @NotBlank(message = "荷送人名は必須です")
    private String shipperName;

    @Column(name = "CONSIGNEE_NAME", length = 100, nullable = false)
    @NotBlank(message = "荷受人名は必須です")
    private String consigneeName;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    public enum CargoStatus {
        PENDING("待機中"),
        IN_TRANSIT("輸送中"),
        ARRIVED("到着済み"),
        DELIVERED("配達済み"),
        CANCELLED("キャンセル");

        private final String displayName;

        CargoStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
} 