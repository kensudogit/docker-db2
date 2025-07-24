package com.aircargo.controller;

import com.aircargo.entity.Outbound;
import com.aircargo.service.CargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outbound")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OutboundController {

    private final CargoService cargoService;

    @GetMapping
    public ResponseEntity<List<Outbound>> getAllOutbounds() {
        List<Outbound> outbounds = cargoService.getAllOutbounds();
        return ResponseEntity.ok(outbounds);
    }

    @GetMapping("/{outboundId}")
    public ResponseEntity<Outbound> getOutboundById(@PathVariable String outboundId) {
        return cargoService.getOutboundById(outboundId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Outbound> createOutbound(@RequestBody Outbound outbound) {
        Outbound createdOutbound = cargoService.createOutbound(outbound);
        return ResponseEntity.ok(createdOutbound);
    }

    @PutMapping("/{outboundId}/status")
    public ResponseEntity<Outbound> updateOutboundStatus(@PathVariable String outboundId, @RequestParam String status) {
        try {
            Outbound updatedOutbound = cargoService.updateOutboundStatus(outboundId, status);
            return ResponseEntity.ok(updatedOutbound);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cargo/{cargoId}")
    public ResponseEntity<List<Outbound>> getOutboundsByCargoId(@PathVariable String cargoId) {
        List<Outbound> outbounds = cargoService.getOutboundsByCargoId(cargoId);
        return ResponseEntity.ok(outbounds);
    }
} 