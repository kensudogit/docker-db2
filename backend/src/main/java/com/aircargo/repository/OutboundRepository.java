package com.aircargo.repository;

import com.aircargo.entity.Outbound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OutboundRepository extends JpaRepository<Outbound, String> {

    List<Outbound> findByCargoId(String cargoId);

    List<Outbound> findByDepartureDate(LocalDate departureDate);

    List<Outbound> findByStatus(String status);
} 