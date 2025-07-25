package com.aircargo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * 出荷エンティティクラス
 * 
 * 貨物の出荷情報を管理するエンティティです。
 * 貨物が空港から出発する際の出荷記録を保持し、
 * 出発日時、ターミナル、ステータス、担当者情報などを管理します。
 */
@Entity
@Table(name = "OUTBOUND")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Outbound {

    /**
     * 出荷ID（主キー）
     * 出荷記録を一意に識別するID
     */
    @Id
    @Column(name = "OUTBOUND_ID")
    private String outboundId;

    /**
     * 貨物ID
     * 関連する貨物のID（外部キー）
     */
    @Column(name = "CARGO_ID")
    private String cargoId;

    /**
     * フライト番号
     * 出荷する貨物が搭載されるフライト番号
     */
    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;

    /**
     * 出発日
     * 貨物の出発日
     */
    @Column(name = "DEPARTURE_DATE")
    private LocalDate departureDate;

    /**
     * 出発時刻
     * 貨物の出発時刻
     */
    @Column(name = "DEPARTURE_TIME")
    private LocalTime departureTime;

    /**
     * ターミナル
     * 貨物が出発するターミナル番号
     */
    @Column(name = "TERMINAL")
    private String terminal;

    /**
     * ステータス
     * 出荷の現在の状態（SCHEDULED、READY、DEPARTED等）
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 担当者ID
     * 出荷処理を担当する作業員のID
     */
    @Column(name = "HANDLER_ID")
    private String handlerId;

    /**
     * 備考
     * 出荷に関する追加情報や特記事項
     */
    @Column(name = "NOTES")
    private String notes;

    /**
     * 作成日時
     * 出荷レコードの作成日時
     */
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    /**
     * バージョン番号
     * 楽観的ロック制御用のバージョン番号
     */
    @Version
    @Column(name = "VERSION")
    private Integer version;
} 