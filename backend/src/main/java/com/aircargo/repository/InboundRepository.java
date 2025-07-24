package com.aircargo.repository;

import com.aircargo.entity.Inbound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, String> {

    List<Inbound> findByCargoId(String cargoId);

    List<Inbound> findByArrivalDate(LocalDate arrivalDate);

    List<Inbound> findByStatus(String status);
} 