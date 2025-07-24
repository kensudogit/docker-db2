package com.aircargo.controller;

import com.aircargo.entity.Inbound;
import com.aircargo.service.CargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inbound")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InboundController {

    private final CargoService cargoService;

    @GetMapping
    public ResponseEntity<List<Inbound>> getAllInbounds() {
        List<Inbound> inbounds = cargoService.getAllInbounds();
        return ResponseEntity.ok(inbounds);
    }

    @GetMapping("/{inboundId}")
    public ResponseEntity<Inbound> getInboundById(@PathVariable String inboundId) {
        return cargoService.getInboundById(inboundId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Inbound> createInbound(@RequestBody Inbound inbound) {
        Inbound createdInbound = cargoService.createInbound(inbound);
        return ResponseEntity.ok(createdInbound);
    }

    @PutMapping("/{inboundId}/status")
    public ResponseEntity<Inbound> updateInboundStatus(@PathVariable String inboundId, @RequestParam String status) {
        try {
            Inbound updatedInbound = cargoService.updateInboundStatus(inboundId, status);
            return ResponseEntity.ok(updatedInbound);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cargo/{cargoId}")
    public ResponseEntity<List<Inbound>> getInboundsByCargoId(@PathVariable String cargoId) {
        List<Inbound> inbounds = cargoService.getInboundsByCargoId(cargoId);
        return ResponseEntity.ok(inbounds);
    }
} 