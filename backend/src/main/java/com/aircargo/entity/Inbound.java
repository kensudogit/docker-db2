package com.aircargo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * 入荷エンティティクラス
 * 
 * 貨物の入荷情報を管理するエンティティです。
 * 貨物が空港に到着した際の入荷記録を保持し、
 * 到着日時、ターミナル、ステータス、担当者情報などを管理します。
 */
@Entity
@Table(name = "INBOUND")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inbound {

    /**
     * 入荷ID（主キー）
     * 入荷記録を一意に識別するID
     */
    @Id
    @Column(name = "INBOUND_ID")
    private String inboundId;

    /**
     * 貨物ID
     * 関連する貨物のID（外部キー）
     */
    @Column(name = "CARGO_ID")
    private String cargoId;

    /**
     * フライト番号
     * 入荷した貨物が搭載されていたフライト番号
     */
    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;

    /**
     * 到着日
     * 貨物の到着日
     */
    @Column(name = "ARRIVAL_DATE")
    private LocalDate arrivalDate;

    /**
     * 到着時刻
     * 貨物の到着時刻
     */
    @Column(name = "ARRIVAL_TIME")
    private LocalTime arrivalTime;

    /**
     * ターミナル
     * 貨物が到着したターミナル番号
     */
    @Column(name = "TERMINAL")
    private String terminal;

    /**
     * ステータス
     * 入荷の現在の状態（ARRIVED、PROCESSING、COMPLETED等）
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 担当者ID
     * 入荷処理を担当する作業員のID
     */
    @Column(name = "HANDLER_ID")
    private String handlerId;

    /**
     * 備考
     * 入荷に関する追加情報や特記事項
     */
    @Column(name = "NOTES")
    private String notes;

    /**
     * 作成日時
     * 入荷レコードの作成日時
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