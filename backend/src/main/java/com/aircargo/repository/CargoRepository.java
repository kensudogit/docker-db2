package com.aircargo.repository;

import com.aircargo.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 貨物リポジトリインターフェース
 * 
 * 貨物エンティティのデータアクセス操作を提供します。
 * Spring Data JPAを使用して基本的なCRUD操作に加え、
 * フライト番号、ステータス、荷送人名、荷受人名による検索機能を提供します。
 */
@Repository
public interface CargoRepository extends JpaRepository<Cargo, String> {

    /**
     * フライト番号による貨物検索
     * 
     * @param flightNumber フライト番号
     * @return 指定されたフライト番号の貨物リスト
     */
    List<Cargo> findByFlightNumber(String flightNumber);

    /**
     * ステータスによる貨物検索
     * 
     * @param status 貨物ステータス
     * @return 指定されたステータスの貨物リスト
     */
    List<Cargo> findByStatus(String status);

    /**
     * 荷送人名による貨物検索（大文字小文字を区別しない）
     * 
     * @param shipperName 荷送人名（部分一致）
     * @return 指定された荷送人名を含む貨物リスト
     */
    List<Cargo> findByShipperNameContainingIgnoreCase(String shipperName);

    /**
     * 荷受人名による貨物検索（大文字小文字を区別しない）
     * 
     * @param consigneeName 荷受人名（部分一致）
     * @return 指定された荷受人名を含む貨物リスト
     */
    List<Cargo> findByConsigneeNameContainingIgnoreCase(String consigneeName);

    /**
     * ステータス別貨物数のカウント
     * 
     * @param status 貨物ステータス
     * @return 指定されたステータスの貨物数
     */
    @Query("SELECT COUNT(c) FROM Cargo c WHERE c.status = :status")
    Long countByStatus(@Param("status") String status);
} 