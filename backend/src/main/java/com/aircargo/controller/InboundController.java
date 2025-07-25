package com.aircargo.controller;

import com.aircargo.entity.Inbound;
import com.aircargo.service.CargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 入荷管理RESTコントローラー
 * 
 * 入荷に関するREST APIエンドポイントを提供します。
 * 入荷記録のCRUD操作、ステータス更新、貨物IDによる検索などの機能を提供します。
 */
@RestController
@RequestMapping("/api/inbound")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InboundController {

    private final CargoService cargoService;

    /**
     * 全入荷記録の取得
     * 
     * @return 全入荷記録のリスト
     */
    @GetMapping
    public ResponseEntity<List<Inbound>> getAllInbounds() {
        List<Inbound> inbounds = cargoService.getAllInbounds();
        return ResponseEntity.ok(inbounds);
    }

    /**
     * 入荷IDによる入荷記録取得
     * 
     * @param inboundId 入荷ID
     * @return 指定された入荷IDの入荷記録
     */
    @GetMapping("/{inboundId}")
    public ResponseEntity<Inbound> getInboundById(@PathVariable String inboundId) {
        return cargoService.getInboundById(inboundId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 新規入荷記録の作成
     * 
     * @param inbound 作成する入荷記録
     * @return 作成された入荷記録
     */
    @PostMapping
    public ResponseEntity<Inbound> createInbound(@RequestBody Inbound inbound) {
        Inbound createdInbound = cargoService.createInbound(inbound);
        return ResponseEntity.ok(createdInbound);
    }

    /**
     * 入荷ステータスの更新
     * 
     * @param inboundId 更新する入荷ID
     * @param status 新しいステータス
     * @return 更新された入荷記録
     */
    @PutMapping("/{inboundId}/status")
    public ResponseEntity<Inbound> updateInboundStatus(@PathVariable String inboundId, @RequestParam String status) {
        try {
            Inbound updatedInbound = cargoService.updateInboundStatus(inboundId, status);
            return ResponseEntity.ok(updatedInbound);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 貨物IDによる入荷記録検索
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物IDの入荷記録リスト
     */
    @GetMapping("/cargo/{cargoId}")
    public ResponseEntity<List<Inbound>> getInboundsByCargoId(@PathVariable String cargoId) {
        List<Inbound> inbounds = cargoService.getInboundsByCargoId(cargoId);
        return ResponseEntity.ok(inbounds);
    }
} 