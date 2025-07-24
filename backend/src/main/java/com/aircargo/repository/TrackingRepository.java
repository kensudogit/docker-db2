package com.aircargo.repository;

import com.aircargo.entity.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, String> {

    List<Tracking> findByCargoIdOrderByTimestampDesc(String cargoId);

    List<Tracking> findByStatus(String status);

    List<Tracking> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
} 