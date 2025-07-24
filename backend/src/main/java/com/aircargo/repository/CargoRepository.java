package com.aircargo.repository;

import com.aircargo.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, String> {

    List<Cargo> findByFlightNumber(String flightNumber);

    List<Cargo> findByStatus(String status);

    List<Cargo> findByShipperNameContainingIgnoreCase(String shipperName);

    List<Cargo> findByConsigneeNameContainingIgnoreCase(String consigneeName);

    @Query("SELECT COUNT(c) FROM Cargo c WHERE c.status = :status")
    Long countByStatus(@Param("status") String status);
} 