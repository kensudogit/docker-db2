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

@Service
@RequiredArgsConstructor
@Transactional
public class CargoService {

    private final CargoRepository cargoRepository;
    private final InboundRepository inboundRepository;
    private final OutboundRepository outboundRepository;
    private final TrackingRepository trackingRepository;

    public List<Cargo> getAllCargos() {
        return cargoRepository.findAll();
    }

    public Optional<Cargo> getCargoById(String cargoId) {
        return cargoRepository.findById(cargoId);
    }

    public List<Cargo> getCargosByFlightNumber(String flightNumber) {
        return cargoRepository.findByFlightNumber(flightNumber);
    }

    public List<Cargo> getCargosByStatus(String status) {
        return cargoRepository.findByStatus(status);
    }

    public List<Cargo> searchCargosByShipperName(String shipperName) {
        return cargoRepository.findByShipperNameContainingIgnoreCase(shipperName);
    }

    public List<Cargo> searchCargosByConsigneeName(String consigneeName) {
        return cargoRepository.findByConsigneeNameContainingIgnoreCase(consigneeName);
    }

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

    public void deleteCargo(String cargoId) {
        Cargo cargo = cargoRepository.findById(cargoId)
            .orElseThrow(() -> new RuntimeException("貨物が見つかりません: " + cargoId));
        cargoRepository.delete(cargo);
    }

    public Cargo updateCargoStatus(String cargoId, String status) {
        Cargo cargo = cargoRepository.findById(cargoId)
            .orElseThrow(() -> new RuntimeException("貨物が見つかりません: " + cargoId));

        cargo.setStatus(status);
        cargo.setUpdatedDate(LocalDateTime.now());

        return cargoRepository.save(cargo);
    }

    // Inbound関連
    public List<Inbound> getAllInbounds() {
        return inboundRepository.findAll();
    }

    public Optional<Inbound> getInboundById(String inboundId) {
        return inboundRepository.findById(inboundId);
    }

    public List<Inbound> getInboundsByCargoId(String cargoId) {
        return inboundRepository.findByCargoId(cargoId);
    }

    public Inbound createInbound(Inbound inbound) {
        String inboundId = "IN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        inbound.setInboundId(inboundId);
        inbound.setStatus("ARRIVED");
        inbound.setCreatedDate(LocalDateTime.now());
        inbound.setVersion(1);

        return inboundRepository.save(inbound);
    }

    public Inbound updateInboundStatus(String inboundId, String status) {
        Inbound inbound = inboundRepository.findById(inboundId)
            .orElseThrow(() -> new RuntimeException("入荷記録が見つかりません: " + inboundId));

        inbound.setStatus(status);

        return inboundRepository.save(inbound);
    }

    // Outbound関連
    public List<Outbound> getAllOutbounds() {
        return outboundRepository.findAll();
    }

    public Optional<Outbound> getOutboundById(String outboundId) {
        return outboundRepository.findById(outboundId);
    }

    public List<Outbound> getOutboundsByCargoId(String cargoId) {
        return outboundRepository.findByCargoId(cargoId);
    }

    public Outbound createOutbound(Outbound outbound) {
        String outboundId = "OUT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        outbound.setOutboundId(outboundId);
        outbound.setStatus("SCHEDULED");
        outbound.setCreatedDate(LocalDateTime.now());
        outbound.setVersion(1);

        return outboundRepository.save(outbound);
    }

    public Outbound updateOutboundStatus(String outboundId, String status) {
        Outbound outbound = outboundRepository.findById(outboundId)
            .orElseThrow(() -> new RuntimeException("出荷記録が見つかりません: " + outboundId));

        outbound.setStatus(status);

        return outboundRepository.save(outbound);
    }

    // Tracking関連
    public List<Tracking> getAllTrackings() {
        return trackingRepository.findAll();
    }

    public Optional<Tracking> getTrackingById(String trackingId) {
        return trackingRepository.findById(trackingId);
    }

    public List<Tracking> getTrackingsByCargoId(String cargoId) {
        return trackingRepository.findByCargoIdOrderByTimestampDesc(cargoId);
    }

    public Tracking createTracking(Tracking tracking) {
        String trackingId = "TR" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        tracking.setTrackingId(trackingId);
        tracking.setTimestamp(LocalDateTime.now());
        tracking.setVersion(1);

        return trackingRepository.save(tracking);
    }

    public List<Tracking> getTrackingHistory(String cargoId) {
        return trackingRepository.findByCargoIdOrderByTimestampDesc(cargoId);
    }
} 