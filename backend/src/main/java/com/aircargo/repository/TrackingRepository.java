package com.aircargo.repository;

import com.aircargo.entity.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, String> {

    List<Tracking> findByCargoIdOrderByTimestampDesc(String cargoId);
    
    List<Tracking> findByCargoId(String cargoId);
    
    List<Tracking> findByStatus(String status);
    
    List<Tracking> findByLocation(String location);
    
    List<Tracking> findByHandlerId(String handlerId);
    
    List<Tracking> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    List<Tracking> findByCargoIdAndTimestampBetween(String cargoId, LocalDateTime startTime, LocalDateTime endTime);
} 