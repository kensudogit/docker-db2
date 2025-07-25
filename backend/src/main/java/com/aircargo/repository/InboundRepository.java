package com.aircargo.repository;

import com.aircargo.entity.Inbound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 入荷リポジトリインターフェース
 * 
 * 入荷エンティティのデータアクセス操作を提供します。
 * Spring Data JPAを使用して基本的なCRUD操作に加え、
 * 貨物ID、到着日、ステータスによる検索機能を提供します。
 */
@Repository
public interface InboundRepository extends JpaRepository<Inbound, String> {

    /**
     * 貨物IDによる入荷記録検索
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物IDの入荷記録リスト
     */
    List<Inbound> findByCargoId(String cargoId);

    /**
     * 到着日による入荷記録検索
     * 
     * @param arrivalDate 到着日
     * @return 指定された到着日の入荷記録リスト
     */
    List<Inbound> findByArrivalDate(LocalDate arrivalDate);

    /**
     * ステータスによる入荷記録検索
     * 
     * @param status 入荷ステータス
     * @return 指定されたステータスの入荷記録リスト
     */
    List<Inbound> findByStatus(String status);
} 