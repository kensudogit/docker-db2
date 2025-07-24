package com.aircargo.repository;

import com.aircargo.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, String> {

    List<Cargo> findByFlightNumber(String flightNumber);
    
    List<Cargo> findByStatus(Cargo.CargoStatus status);
    
    List<Cargo> findByOriginAirport(String originAirport);
    
    List<Cargo> findByDestinationAirport(String destinationAirport);
    
    @Query("SELECT c FROM Cargo c WHERE c.shipperName LIKE %:shipperName%")
    List<Cargo> findByShipperNameContaining(@Param("shipperName") String shipperName);
    
    @Query("SELECT c FROM Cargo c WHERE c.consigneeName LIKE %:consigneeName%")
    List<Cargo> findByConsigneeNameContaining(@Param("consigneeName") String consigneeName);
    
    @Query("SELECT c FROM Cargo c WHERE c.cargoType = :cargoType")
    List<Cargo> findByCargoType(@Param("cargoType") String cargoType);
    
    @Query("SELECT c FROM Cargo c WHERE c.weight BETWEEN :minWeight AND :maxWeight")
    List<Cargo> findByWeightBetween(@Param("minWeight") Double minWeight, @Param("maxWeight") Double maxWeight);
    
    @Query("SELECT c FROM Cargo c WHERE c.originAirport = :originAirport AND c.destinationAirport = :destinationAirport")
    List<Cargo> findByOriginAndDestination(@Param("originAirport") String originAirport, 
                                          @Param("destinationAirport") String destinationAirport);
    
    @Query("SELECT COUNT(c) FROM Cargo c WHERE c.status = :status")
    Long countByStatus(@Param("status") Cargo.CargoStatus status);
    
    @Query("SELECT c FROM Cargo c ORDER BY c.createdDate DESC")
    List<Cargo> findAllOrderByCreatedDateDesc();
} 