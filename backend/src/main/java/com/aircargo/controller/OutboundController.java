package com.aircargo.controller;

import com.aircargo.entity.Outbound;
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
@RequestMapping("/outbound")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "出荷管理", description = "出荷のCRUD操作")
@CrossOrigin(origins = "*")
public class OutboundController {

    private final CargoService cargoService;

    @GetMapping
    @Operation(summary = "全出荷取得", description = "全ての出荷情報を取得します")
    public ResponseEntity<List<Outbound>> getAllOutbound() {
        List<Outbound> outboundList = cargoService.getAllOutbound();
        return ResponseEntity.ok(outboundList);
    }

    @GetMapping("/{outboundId}")
    @Operation(summary = "出荷詳細取得", description = "指定された出荷IDの詳細情報を取得します")
    public ResponseEntity<Outbound> getOutboundById(@PathVariable String outboundId) {
        Optional<Outbound> outbound = cargoService.getOutboundById(outboundId);
        return outbound.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "出荷作成", description = "新しい出荷を登録します")
    public ResponseEntity<Outbound> createOutbound(@Valid @RequestBody Outbound outbound) {
        Outbound createdOutbound = cargoService.createOutbound(outbound);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOutbound);
    }

    @PutMapping("/{outboundId}/status")
    @Operation(summary = "出荷ステータス更新", description = "指定された出荷IDのステータスを更新します")
    public ResponseEntity<Outbound> updateOutboundStatus(@PathVariable String outboundId, 
                                                         @RequestParam Outbound.OutboundStatus status) {
        try {
            Outbound updatedOutbound = cargoService.updateOutboundStatus(outboundId, status);
            return ResponseEntity.ok(updatedOutbound);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 