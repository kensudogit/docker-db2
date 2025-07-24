package com.aircargo.controller;

import com.aircargo.entity.Inbound;
import com.aircargo.service.CargoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inbound")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "入荷管理", description = "入荷のCRUD操作")
@CrossOrigin(origins = "*")
public class InboundController {

    private final CargoService cargoService;

    @GetMapping
    @Operation(summary = "全入荷取得", description = "全ての入荷情報を取得します")
    public ResponseEntity<List<Inbound>> getAllInbound() {
        List<Inbound> inboundList = cargoService.getAllInbound();
        return ResponseEntity.ok(inboundList);
    }

    @GetMapping("/{inboundId}")
    @Operation(summary = "入荷詳細取得", description = "指定された入荷IDの詳細情報を取得します")
    public ResponseEntity<Inbound> getInboundById(@PathVariable String inboundId) {
        Optional<Inbound> inbound = cargoService.getInboundById(inboundId);
        return inbound.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "入荷作成", description = "新しい入荷を登録します")
    public ResponseEntity<Inbound> createInbound(@Valid @RequestBody Inbound inbound) {
        Inbound createdInbound = cargoService.createInbound(inbound);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInbound);
    }

    @PutMapping("/{inboundId}/status")
    @Operation(summary = "入荷ステータス更新", description = "指定された入荷IDのステータスを更新します")
    public ResponseEntity<Inbound> updateInboundStatus(@PathVariable String inboundId, 
                                                       @RequestParam Inbound.InboundStatus status) {
        try {
            Inbound updatedInbound = cargoService.updateInboundStatus(inboundId, status);
            return ResponseEntity.ok(updatedInbound);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 