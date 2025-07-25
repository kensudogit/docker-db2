package com.aircargo.service;

import com.aircargo.repository.CargoRepository;
import com.aircargo.repository.InboundRepository;
import com.aircargo.repository.OutboundRepository;
import com.aircargo.repository.TrackingRepository;
import com.aircargo.entity.Cargo;
import com.aircargo.entity.Inbound;
import com.aircargo.entity.Outbound;
import com.aircargo.entity.Tracking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 貨物管理サービスクラス
 * 
 * 貨物、入荷、出荷、追跡に関するビジネスロジックを提供します。
 * 各エンティティのCRUD操作、検索機能、ステータス管理などの機能を実装します。
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CargoService {

    private final CargoRepository cargoRepository;
    private final InboundRepository inboundRepository;
    private final OutboundRepository outboundRepository;
    private final TrackingRepository trackingRepository;

    /**
     * 全貨物の取得
     * 
     * @return 全貨物のリスト
     */
    public List<Cargo> getAllCargos() {
        return cargoRepository.findAll();
    }

    /**
     * 貨物IDによる貨物取得
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物IDの貨物情報（Optional）
     */
    public Optional<Cargo> getCargoById(String cargoId) {
        return cargoRepository.findById(cargoId);
    }

    /**
     * フライト番号による貨物検索
     * 
     * @param flightNumber フライト番号
     * @return 指定されたフライト番号の貨物リスト
     */
    public List<Cargo> getCargosByFlightNumber(String flightNumber) {
        return cargoRepository.findByFlightNumber(flightNumber);
    }

    /**
     * ステータスによる貨物検索
     * 
     * @param status 貨物ステータス
     * @return 指定されたステータスの貨物リスト
     */
    public List<Cargo> getCargosByStatus(String status) {
        return cargoRepository.findByStatus(status);
    }

    /**
     * 荷送人名による貨物検索
     * 
     * @param shipperName 荷送人名
     * @return 指定された荷送人名を含む貨物リスト
     */
    public List<Cargo> searchCargosByShipperName(String shipperName) {
        return cargoRepository.findByShipperNameContainingIgnoreCase(shipperName);
    }

    /**
     * 荷受人名による貨物検索
     * 
     * @param consigneeName 荷受人名
     * @return 指定された荷受人名を含む貨物リスト
     */
    public List<Cargo> searchCargosByConsigneeName(String consigneeName) {
        return cargoRepository.findByConsigneeNameContainingIgnoreCase(consigneeName);
    }

    /**
     * 新規貨物の作成
     * 
     * @param cargo 作成する貨物情報
     * @return 作成された貨物情報
     */
    public Cargo createCargo(Cargo cargo) {
        // 貨物IDを生成
        String cargoId = "C" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        cargo.setCargoId(cargoId);
        cargo.setStatus("PENDING");
        cargo.setCreatedDate(LocalDateTime.now());
        cargo.setUpdatedDate(LocalDateTime.now());
        cargo.setVersion(1);

        return cargoRepository.save(cargo);
    }

    /**
     * 貨物情報の更新
     * 
     * @param cargoId 更新する貨物ID
     * @param cargoDetails 更新する貨物情報
     * @return 更新された貨物情報
     * @throws RuntimeException 貨物が見つからない場合
     */
    public Cargo updateCargo(String cargoId, Cargo cargoDetails) {
        Cargo existingCargo = cargoRepository.findById(cargoId)
            .orElseThrow(() -> new RuntimeException("貨物が見つかりません: " + cargoId));

        // 更新可能なフィールドのみ更新
        existingCargo.setFlightNumber(cargoDetails.getFlightNumber());
        existingCargo.setOriginAirport(cargoDetails.getOriginAirport());
        existingCargo.setDestinationAirport(cargoDetails.getDestinationAirport());
        existingCargo.setCargoType(cargoDetails.getCargoType());
        existingCargo.setWeight(cargoDetails.getWeight());
        existingCargo.setVolume(cargoDetails.getVolume());
        existingCargo.setStatus(cargoDetails.getStatus());
        existingCargo.setShipperName(cargoDetails.getShipperName());
        existingCargo.setConsigneeName(cargoDetails.getConsigneeName());
        existingCargo.setUpdatedDate(LocalDateTime.now());

        return cargoRepository.save(existingCargo);
    }

    /**
     * 貨物の削除
     * 
     * @param cargoId 削除する貨物ID
     * @throws RuntimeException 貨物が見つからない場合
     */
    public void deleteCargo(String cargoId) {
        Cargo cargo = cargoRepository.findById(cargoId)
            .orElseThrow(() -> new RuntimeException("貨物が見つかりません: " + cargoId));
        cargoRepository.delete(cargo);
    }

    /**
     * 貨物ステータスの更新
     * 
     * @param cargoId 更新する貨物ID
     * @param status 新しいステータス
     * @return 更新された貨物情報
     * @throws RuntimeException 貨物が見つからない場合
     */
    public Cargo updateCargoStatus(String cargoId, String status) {
        Cargo cargo = cargoRepository.findById(cargoId)
            .orElseThrow(() -> new RuntimeException("貨物が見つかりません: " + cargoId));

        cargo.setStatus(status);
        cargo.setUpdatedDate(LocalDateTime.now());

        return cargoRepository.save(cargo);
    }

    // ==================== 入荷関連メソッド ====================

    /**
     * 全入荷記録の取得
     * 
     * @return 全入荷記録のリスト
     */
    public List<Inbound> getAllInbounds() {
        return inboundRepository.findAll();
    }

    /**
     * 入荷IDによる入荷記録取得
     * 
     * @param inboundId 入荷ID
     * @return 指定された入荷IDの入荷記録（Optional）
     */
    public Optional<Inbound> getInboundById(String inboundId) {
        return inboundRepository.findById(inboundId);
    }

    /**
     * 貨物IDによる入荷記録検索
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物IDの入荷記録リスト
     */
    public List<Inbound> getInboundsByCargoId(String cargoId) {
        return inboundRepository.findByCargoId(cargoId);
    }

    /**
     * 新規入荷記録の作成
     * 
     * @param inbound 作成する入荷記録
     * @return 作成された入荷記録
     */
    public Inbound createInbound(Inbound inbound) {
        String inboundId = "IN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        inbound.setInboundId(inboundId);
        inbound.setStatus("ARRIVED");
        inbound.setCreatedDate(LocalDateTime.now());
        inbound.setVersion(1);

        return inboundRepository.save(inbound);
    }

    /**
     * 入荷ステータスの更新
     * 
     * @param inboundId 更新する入荷ID
     * @param status 新しいステータス
     * @return 更新された入荷記録
     * @throws RuntimeException 入荷記録が見つからない場合
     */
    public Inbound updateInboundStatus(String inboundId, String status) {
        Inbound inbound = inboundRepository.findById(inboundId)
            .orElseThrow(() -> new RuntimeException("入荷記録が見つかりません: " + inboundId));

        inbound.setStatus(status);

        return inboundRepository.save(inbound);
    }

    // ==================== 出荷関連メソッド ====================

    /**
     * 全出荷記録の取得
     * 
     * @return 全出荷記録のリスト
     */
    public List<Outbound> getAllOutbounds() {
        return outboundRepository.findAll();
    }

    /**
     * 出荷IDによる出荷記録取得
     * 
     * @param outboundId 出荷ID
     * @return 指定された出荷IDの出荷記録（Optional）
     */
    public Optional<Outbound> getOutboundById(String outboundId) {
        return outboundRepository.findById(outboundId);
    }

    /**
     * 貨物IDによる出荷記録検索
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物IDの出荷記録リスト
     */
    public List<Outbound> getOutboundsByCargoId(String cargoId) {
        return outboundRepository.findByCargoId(cargoId);
    }

    /**
     * 新規出荷記録の作成
     * 
     * @param outbound 作成する出荷記録
     * @return 作成された出荷記録
     */
    public Outbound createOutbound(Outbound outbound) {
        String outboundId = "OUT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        outbound.setOutboundId(outboundId);
        outbound.setStatus("SCHEDULED");
        outbound.setCreatedDate(LocalDateTime.now());
        outbound.setVersion(1);

        return outboundRepository.save(outbound);
    }

    /**
     * 出荷ステータスの更新
     * 
     * @param outboundId 更新する出荷ID
     * @param status 新しいステータス
     * @return 更新された出荷記録
     * @throws RuntimeException 出荷記録が見つからない場合
     */
    public Outbound updateOutboundStatus(String outboundId, String status) {
        Outbound outbound = outboundRepository.findById(outboundId)
            .orElseThrow(() -> new RuntimeException("出荷記録が見つかりません: " + outboundId));

        outbound.setStatus(status);

        return outboundRepository.save(outbound);
    }

    // ==================== 追跡関連メソッド ====================

    /**
     * 全追跡記録の取得
     * 
     * @return 全追跡記録のリスト
     */
    public List<Tracking> getAllTrackings() {
        return trackingRepository.findAll();
    }

    /**
     * 追跡IDによる追跡記録取得
     * 
     * @param trackingId 追跡ID
     * @return 指定された追跡IDの追跡記録（Optional）
     */
    public Optional<Tracking> getTrackingById(String trackingId) {
        return trackingRepository.findById(trackingId);
    }

    /**
     * 貨物IDによる追跡記録検索
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物IDの追跡記録リスト（最新順）
     */
    public List<Tracking> getTrackingsByCargoId(String cargoId) {
        return trackingRepository.findByCargoIdOrderByTimestampDesc(cargoId);
    }

    /**
     * 新規追跡記録の作成
     * 
     * @param tracking 作成する追跡記録
     * @return 作成された追跡記録
     */
    public Tracking createTracking(Tracking tracking) {
        String trackingId = "TR" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        tracking.setTrackingId(trackingId);
        tracking.setTimestamp(LocalDateTime.now());
        tracking.setVersion(1);

        return trackingRepository.save(tracking);
    }

    /**
     * 貨物の追跡履歴取得
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物の追跡履歴リスト（最新順）
     */
    public List<Tracking> getTrackingHistory(String cargoId) {
        return trackingRepository.findByCargoIdOrderByTimestampDesc(cargoId);
    }
} 