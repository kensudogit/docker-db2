package com.aircargo.repository;

import com.aircargo.entity.Inbound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, String> {

    List<Inbound> findByCargoId(String cargoId);
    
    List<Inbound> findByFlightNumber(String flightNumber);
    
    List<Inbound> findByStatus(Inbound.InboundStatus status);
    
    List<Inbound> findByArrivalDate(LocalDate arrivalDate);
    
    List<Inbound> findByArrivalDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Inbound> findByHandlerId(String handlerId);
    
    List<Inbound> findByTerminal(String terminal);
} 