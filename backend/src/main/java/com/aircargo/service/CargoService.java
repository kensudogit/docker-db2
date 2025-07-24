package com.aircargo.service;

import com.aircargo.entity.Cargo;
import com.aircargo.entity.Inbound;
import com.aircargo.entity.Outbound;
import com.aircargo.entity.Tracking;
import com.aircargo.repository.CargoRepository;
import com.aircargo.repository.InboundRepository;
import com.aircargo.repository.OutboundRepository;
import com.aircargo.repository.TrackingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CargoService {

    private final CargoRepository cargoRepository;
    private final InboundRepository inboundRepository;
    private final OutboundRepository outboundRepository;
    private final TrackingRepository trackingRepository;

    // 貨物関連の操作
    public List<Cargo> getAllCargo() {
        return cargoRepository.findAllOrderByCreatedDateDesc();
    }

    public Optional<Cargo> getCargoById(String cargoId) {
        return cargoRepository.findById(cargoId);
    }

    public Cargo createCargo(Cargo cargo) {
        if (cargo.getCargoId() == null || cargo.getCargoId().isEmpty()) {
            cargo.setCargoId("CARGO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        cargo.setCreatedDate(LocalDateTime.now());
        cargo.setUpdatedDate(LocalDateTime.now());
        return cargoRepository.save(cargo);
    }

    public Cargo updateCargo(String cargoId, Cargo cargoDetails) {
        return cargoRepository.findById(cargoId)
                .map(cargo -> {
                    cargo.setFlightNumber(cargoDetails.getFlightNumber());
                    cargo.setOriginAirport(cargoDetails.getOriginAirport());
                    cargo.setDestinationAirport(cargoDetails.getDestinationAirport());
                    cargo.setCargoType(cargoDetails.getCargoType());
                    cargo.setWeight(cargoDetails.getWeight());
                    cargo.setVolume(cargoDetails.getVolume());
                    cargo.setStatus(cargoDetails.getStatus());
                    cargo.setShipperName(cargoDetails.getShipperName());
                    cargo.setConsigneeName(cargoDetails.getConsigneeName());
                    cargo.setUpdatedDate(LocalDateTime.now());
                    return cargoRepository.save(cargo);
                })
                .orElseThrow(() -> new RuntimeException("貨物が見つかりません: " + cargoId));
    }

    public void deleteCargo(String cargoId) {
        cargoRepository.deleteById(cargoId);
    }

    public List<Cargo> getCargoByStatus(Cargo.CargoStatus status) {
        return cargoRepository.findByStatus(status);
    }

    public List<Cargo> getCargoByFlightNumber(String flightNumber) {
        return cargoRepository.findByFlightNumber(flightNumber);
    }

    // 入荷関連の操作
    public List<Inbound> getAllInbound() {
        return inboundRepository.findAll();
    }

    public Optional<Inbound> getInboundById(String inboundId) {
        return inboundRepository.findById(inboundId);
    }

    public Inbound createInbound(Inbound inbound) {
        if (inbound.getInboundId() == null || inbound.getInboundId().isEmpty()) {
            inbound.setInboundId("IN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        inbound.setCreatedDate(LocalDateTime.now());
        
        // 貨物のステータスを更新
        cargoRepository.findById(inbound.getCargoId())
                .ifPresent(cargo -> {
                    cargo.setStatus(Cargo.CargoStatus.ARRIVED);
                    cargo.setUpdatedDate(LocalDateTime.now());
                    cargoRepository.save(cargo);
                });
        
        return inboundRepository.save(inbound);
    }

    public Inbound updateInboundStatus(String inboundId, Inbound.InboundStatus status) {
        return inboundRepository.findById(inboundId)
                .map(inbound -> {
                    inbound.setStatus(status);
                    return inboundRepository.save(inbound);
                })
                .orElseThrow(() -> new RuntimeException("入荷記録が見つかりません: " + inboundId));
    }

    // 出荷関連の操作
    public List<Outbound> getAllOutbound() {
        return outboundRepository.findAll();
    }

    public Optional<Outbound> getOutboundById(String outboundId) {
        return outboundRepository.findById(outboundId);
    }

    public Outbound createOutbound(Outbound outbound) {
        if (outbound.getOutboundId() == null || outbound.getOutboundId().isEmpty()) {
            outbound.setOutboundId("OUT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        outbound.setCreatedDate(LocalDateTime.now());
        
        // 貨物のステータスを更新
        cargoRepository.findById(outbound.getCargoId())
                .ifPresent(cargo -> {
                    cargo.setStatus(Cargo.CargoStatus.IN_TRANSIT);
                    cargo.setUpdatedDate(LocalDateTime.now());
                    cargoRepository.save(cargo);
                });
        
        return outboundRepository.save(outbound);
    }

    public Outbound updateOutboundStatus(String outboundId, Outbound.OutboundStatus status) {
        return outboundRepository.findById(outboundId)
                .map(outbound -> {
                    outbound.setStatus(status);
                    return outboundRepository.save(outbound);
                })
                .orElseThrow(() -> new RuntimeException("出荷記録が見つかりません: " + outboundId));
    }

    // 追跡関連の操作
    public List<Tracking> getTrackingByCargoId(String cargoId) {
        return trackingRepository.findByCargoIdOrderByTimestampDesc(cargoId);
    }

    public Tracking createTracking(Tracking tracking) {
        if (tracking.getTrackingId() == null || tracking.getTrackingId().isEmpty()) {
            tracking.setTrackingId("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        tracking.setTimestamp(LocalDateTime.now());
        return trackingRepository.save(tracking);
    }

    // 統計情報
    public long getCargoCountByStatus(Cargo.CargoStatus status) {
        return cargoRepository.countByStatus(status);
    }

    public List<Cargo> searchCargo(String keyword) {
        // 貨物ID、フライト番号、荷送人名、荷受人名で検索
        List<Cargo> results = cargoRepository.findByShipperNameContaining(keyword);
        results.addAll(cargoRepository.findByConsigneeNameContaining(keyword));
        results.addAll(cargoRepository.findByFlightNumber(keyword));
        
        // 重複を除去
        return results.stream().distinct().toList();
    }
} 