package com.aircargo.controller;

import com.aircargo.entity.Cargo;
import com.aircargo.entity.Tracking;
import com.aircargo.service.CargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargo")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CargoController {

    private final CargoService cargoService;

    @GetMapping
    public ResponseEntity<List<Cargo>> getAllCargos() {
        List<Cargo> cargos = cargoService.getAllCargos();
        return ResponseEntity.ok(cargos);
    }

    @GetMapping("/{cargoId}")
    public ResponseEntity<Cargo> getCargoById(@PathVariable String cargoId) {
        return cargoService.getCargoById(cargoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cargo> createCargo(@RequestBody Cargo cargo) {
        Cargo createdCargo = cargoService.createCargo(cargo);
        return ResponseEntity.ok(createdCargo);
    }

    @PutMapping("/{cargoId}")
    public ResponseEntity<Cargo> updateCargo(@PathVariable String cargoId, @RequestBody Cargo cargoDetails) {
        try {
            Cargo updatedCargo = cargoService.updateCargo(cargoId, cargoDetails);
            return ResponseEntity.ok(updatedCargo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{cargoId}")
    public ResponseEntity<Void> deleteCargo(@PathVariable String cargoId) {
        try {
            cargoService.deleteCargo(cargoId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/flight/{flightNumber}")
    public ResponseEntity<List<Cargo>> getCargosByFlightNumber(@PathVariable String flightNumber) {
        List<Cargo> cargos = cargoService.getCargosByFlightNumber(flightNumber);
        return ResponseEntity.ok(cargos);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Cargo>> getCargosByStatus(@PathVariable String status) {
        List<Cargo> cargos = cargoService.getCargosByStatus(status);
        return ResponseEntity.ok(cargos);
    }

    @GetMapping("/search/shipper")
    public ResponseEntity<List<Cargo>> searchCargosByShipperName(@RequestParam String shipperName) {
        List<Cargo> cargos = cargoService.searchCargosByShipperName(shipperName);
        return ResponseEntity.ok(cargos);
    }

    @GetMapping("/search/consignee")
    public ResponseEntity<List<Cargo>> searchCargosByConsigneeName(@RequestParam String consigneeName) {
        List<Cargo> cargos = cargoService.searchCargosByConsigneeName(consigneeName);
        return ResponseEntity.ok(cargos);
    }

    @PutMapping("/{cargoId}/status")
    public ResponseEntity<Cargo> updateCargoStatus(@PathVariable String cargoId, @RequestParam String status) {
        try {
            Cargo updatedCargo = cargoService.updateCargoStatus(cargoId, status);
            return ResponseEntity.ok(updatedCargo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{cargoId}/tracking")
    public ResponseEntity<List<Tracking>> getTrackingHistory(@PathVariable String cargoId) {
        List<Tracking> trackingHistory = cargoService.getTrackingHistory(cargoId);
        return ResponseEntity.ok(trackingHistory);
    }
} 