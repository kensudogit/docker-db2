package com.aircargo.controller;

import com.aircargo.entity.Outbound;
import com.aircargo.service.CargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 出荷管理RESTコントローラー
 * 
 * 出荷に関するREST APIエンドポイントを提供します。
 * 出荷記録のCRUD操作、ステータス更新、貨物IDによる検索などの機能を提供します。
 */
@RestController
@RequestMapping("/api/outbound")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OutboundController {

    private final CargoService cargoService;

    /**
     * 全出荷記録の取得
     * 
     * @return 全出荷記録のリスト
     */
    @GetMapping
    public ResponseEntity<List<Outbound>> getAllOutbounds() {
        List<Outbound> outbounds = cargoService.getAllOutbounds();
        return ResponseEntity.ok(outbounds);
    }

    /**
     * 出荷IDによる出荷記録取得
     * 
     * @param outboundId 出荷ID
     * @return 指定された出荷IDの出荷記録
     */
    @GetMapping("/{outboundId}")
    public ResponseEntity<Outbound> getOutboundById(@PathVariable String outboundId) {
        return cargoService.getOutboundById(outboundId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 新規出荷記録の作成
     * 
     * @param outbound 作成する出荷記録
     * @return 作成された出荷記録
     */
    @PostMapping
    public ResponseEntity<Outbound> createOutbound(@RequestBody Outbound outbound) {
        Outbound createdOutbound = cargoService.createOutbound(outbound);
        return ResponseEntity.ok(createdOutbound);
    }

    /**
     * 出荷ステータスの更新
     * 
     * @param outboundId 更新する出荷ID
     * @param status 新しいステータス
     * @return 更新された出荷記録
     */
    @PutMapping("/{outboundId}/status")
    public ResponseEntity<Outbound> updateOutboundStatus(@PathVariable String outboundId, @RequestParam String status) {
        try {
            Outbound updatedOutbound = cargoService.updateOutboundStatus(outboundId, status);
            return ResponseEntity.ok(updatedOutbound);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 貨物IDによる出荷記録検索
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物IDの出荷記録リスト
     */
    @GetMapping("/cargo/{cargoId}")
    public ResponseEntity<List<Outbound>> getOutboundsByCargoId(@PathVariable String cargoId) {
        List<Outbound> outbounds = cargoService.getOutboundsByCargoId(cargoId);
        return ResponseEntity.ok(outbounds);
    }
} 