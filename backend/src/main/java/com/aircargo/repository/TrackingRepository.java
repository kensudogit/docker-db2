package com.aircargo.repository;

import com.aircargo.entity.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 追跡リポジトリインターフェース
 * 
 * 追跡エンティティのデータアクセス操作を提供します。
 * Spring Data JPAを使用して基本的なCRUD操作に加え、
 * 貨物ID、ステータス、タイムスタンプ範囲による検索機能を提供します。
 */
@Repository
public interface TrackingRepository extends JpaRepository<Tracking, String> {

    /**
     * 貨物IDによる追跡記録検索（タイムスタンプ降順）
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物IDの追跡記録リスト（最新順）
     */
    List<Tracking> findByCargoIdOrderByTimestampDesc(String cargoId);

    /**
     * ステータスによる追跡記録検索
     * 
     * @param status 追跡ステータス
     * @return 指定されたステータスの追跡記録リスト
     */
    List<Tracking> findByStatus(String status);

    /**
     * タイムスタンプ範囲による追跡記録検索
     * 
     * @param start 開始日時
     * @param end 終了日時
     * @return 指定された期間の追跡記録リスト
     */
    List<Tracking> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
} 