package com.aircargo.repository;

import com.aircargo.entity.Outbound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 出荷リポジトリインターフェース
 * 
 * 出荷エンティティのデータアクセス操作を提供します。
 * Spring Data JPAを使用して基本的なCRUD操作に加え、
 * 貨物ID、出発日、ステータスによる検索機能を提供します。
 */
@Repository
public interface OutboundRepository extends JpaRepository<Outbound, String> {

    /**
     * 貨物IDによる出荷記録検索
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物IDの出荷記録リスト
     */
    List<Outbound> findByCargoId(String cargoId);

    /**
     * 出発日による出荷記録検索
     * 
     * @param departureDate 出発日
     * @return 指定された出発日の出荷記録リスト
     */
    List<Outbound> findByDepartureDate(LocalDate departureDate);

    /**
     * ステータスによる出荷記録検索
     * 
     * @param status 出荷ステータス
     * @return 指定されたステータスの出荷記録リスト
     */
    List<Outbound> findByStatus(String status);
} 