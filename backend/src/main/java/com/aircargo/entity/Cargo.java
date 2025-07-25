package com.aircargo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 貨物エンティティクラス
 * 
 * 航空貨物の基本情報を管理するエンティティです。
 * 貨物ID、フライト番号、発着空港、貨物タイプ、重量、容積、ステータス、
 * 荷送人・荷受人情報、作成・更新日時などの情報を保持します。
 */
@Entity
@Table(name = "CARGO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cargo {

    /**
     * 貨物ID（主キー）
     * 貨物を一意に識別するID
     */
    @Id
    @Column(name = "CARGO_ID")
    private String cargoId;

    /**
     * フライト番号
     * 貨物が搭載されるフライトの番号
     */
    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;

    /**
     * 出発空港
     * 貨物の出発空港コード（例：NRT、HND）
     */
    @Column(name = "ORIGIN_AIRPORT")
    private String originAirport;

    /**
     * 到着空港
     * 貨物の到着空港コード（例：LAX、JFK）
     */
    @Column(name = "DESTINATION_AIRPORT")
    private String destinationAirport;

    /**
     * 貨物タイプ
     * 貨物の種類（例：一般貨物、危険物、生鮮品など）
     */
    @Column(name = "CARGO_TYPE")
    private String cargoType;

    /**
     * 重量（kg）
     * 貨物の重量
     */
    @Column(name = "WEIGHT")
    private BigDecimal weight;

    /**
     * 容積（m³）
     * 貨物の容積
     */
    @Column(name = "VOLUME")
    private BigDecimal volume;

    /**
     * ステータス
     * 貨物の現在の状態（PENDING、IN_TRANSIT、DELIVERED等）
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 荷送人名
     * 貨物の発送者名
     */
    @Column(name = "SHIPPER_NAME")
    private String shipperName;

    /**
     * 荷受人名
     * 貨物の受取人名
     */
    @Column(name = "CONSIGNEE_NAME")
    private String consigneeName;

    /**
     * 作成日時
     * 貨物レコードの作成日時
     */
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    /**
     * 更新日時
     * 貨物レコードの最終更新日時
     */
    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    /**
     * バージョン番号
     * 楽観的ロック制御用のバージョン番号
     */
    @Version
    @Column(name = "VERSION")
    private Integer version;
} 