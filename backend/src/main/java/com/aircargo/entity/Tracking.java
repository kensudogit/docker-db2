package com.aircargo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 追跡エンティティクラス
 * 
 * 貨物の追跡情報を管理するエンティティです。
 * 貨物の移動状況やステータス変更の履歴を保持し、
 * 位置情報、ステータス、タイムスタンプ、担当者情報などを管理します。
 */
@Entity
@Table(name = "TRACKING")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tracking {

    /**
     * 追跡ID（主キー）
     * 追跡記録を一意に識別するID
     */
    @Id
    @Column(name = "TRACKING_ID")
    private String trackingId;

    /**
     * 貨物ID
     * 関連する貨物のID（外部キー）
     */
    @Column(name = "CARGO_ID")
    private String cargoId;

    /**
     * 位置情報
     * 貨物の現在位置（空港名、ターミナル名など）
     */
    @Column(name = "LOCATION")
    private String location;

    /**
     * ステータス
     * 貨物の現在の状態（IN_TRANSIT、ARRIVED、DEPARTED等）
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * タイムスタンプ
     * 追跡記録の作成日時
     */
    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    /**
     * 担当者ID
     * 追跡記録を作成した作業員のID
     */
    @Column(name = "HANDLER_ID")
    private String handlerId;

    /**
     * 備考
     * 追跡に関する追加情報や特記事項
     */
    @Column(name = "NOTES")
    private String notes;

    /**
     * バージョン番号
     * 楽観的ロック制御用のバージョン番号
     */
    @Version
    @Column(name = "VERSION")
    private Integer version;
} 