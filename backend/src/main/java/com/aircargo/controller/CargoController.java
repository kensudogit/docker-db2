package com.aircargo.controller;

import com.aircargo.entity.Cargo;
import com.aircargo.entity.Tracking;
import com.aircargo.service.CargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 貨物管理RESTコントローラー
 * 
 * 貨物に関するREST APIエンドポイントを提供します。
 * 貨物のCRUD操作、検索機能、ステータス更新、追跡履歴取得などの機能を提供します。
 */
@RestController
@RequestMapping("/api/cargo")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CargoController {

    private final CargoService cargoService;

    /**
     * 全貨物の取得
     * 
     * @return 全貨物のリスト
     */
    @GetMapping
    public ResponseEntity<List<Cargo>> getAllCargos() {
        List<Cargo> cargos = cargoService.getAllCargos();
        return ResponseEntity.ok(cargos);
    }

    /**
     * 貨物IDによる貨物取得
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物IDの貨物情報
     */
    @GetMapping("/{cargoId}")
    public ResponseEntity<Cargo> getCargoById(@PathVariable String cargoId) {
        return cargoService.getCargoById(cargoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 新規貨物の作成
     * 
     * @param cargo 作成する貨物情報
     * @return 作成された貨物情報
     */
    @PostMapping
    public ResponseEntity<Cargo> createCargo(@RequestBody Cargo cargo) {
        Cargo createdCargo = cargoService.createCargo(cargo);
        return ResponseEntity.ok(createdCargo);
    }

    /**
     * 貨物情報の更新
     * 
     * @param cargoId 更新する貨物ID
     * @param cargoDetails 更新する貨物情報
     * @return 更新された貨物情報
     */
    @PutMapping("/{cargoId}")
    public ResponseEntity<Cargo> updateCargo(@PathVariable String cargoId, @RequestBody Cargo cargoDetails) {
        try {
            Cargo updatedCargo = cargoService.updateCargo(cargoId, cargoDetails);
            return ResponseEntity.ok(updatedCargo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 貨物の削除
     * 
     * @param cargoId 削除する貨物ID
     * @return 削除成功時のレスポンス
     */
    @DeleteMapping("/{cargoId}")
    public ResponseEntity<Void> deleteCargo(@PathVariable String cargoId) {
        try {
            cargoService.deleteCargo(cargoId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * フライト番号による貨物検索
     * 
     * @param flightNumber フライト番号
     * @return 指定されたフライト番号の貨物リスト
     */
    @GetMapping("/flight/{flightNumber}")
    public ResponseEntity<List<Cargo>> getCargosByFlightNumber(@PathVariable String flightNumber) {
        List<Cargo> cargos = cargoService.getCargosByFlightNumber(flightNumber);
        return ResponseEntity.ok(cargos);
    }

    /**
     * ステータスによる貨物検索
     * 
     * @param status 貨物ステータス
     * @return 指定されたステータスの貨物リスト
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Cargo>> getCargosByStatus(@PathVariable String status) {
        List<Cargo> cargos = cargoService.getCargosByStatus(status);
        return ResponseEntity.ok(cargos);
    }

    /**
     * 荷送人名による貨物検索
     * 
     * @param shipperName 荷送人名
     * @return 指定された荷送人名を含む貨物リスト
     */
    @GetMapping("/search/shipper")
    public ResponseEntity<List<Cargo>> searchCargosByShipperName(@RequestParam String shipperName) {
        List<Cargo> cargos = cargoService.searchCargosByShipperName(shipperName);
        return ResponseEntity.ok(cargos);
    }

    /**
     * 荷受人名による貨物検索
     * 
     * @param consigneeName 荷受人名
     * @return 指定された荷受人名を含む貨物リスト
     */
    @GetMapping("/search/consignee")
    public ResponseEntity<List<Cargo>> searchCargosByConsigneeName(@RequestParam String consigneeName) {
        List<Cargo> cargos = cargoService.searchCargosByConsigneeName(consigneeName);
        return ResponseEntity.ok(cargos);
    }

    /**
     * 貨物ステータスの更新
     * 
     * @param cargoId 更新する貨物ID
     * @param status 新しいステータス
     * @return 更新された貨物情報
     */
    @PutMapping("/{cargoId}/status")
    public ResponseEntity<Cargo> updateCargoStatus(@PathVariable String cargoId, @RequestParam String status) {
        try {
            Cargo updatedCargo = cargoService.updateCargoStatus(cargoId, status);
            return ResponseEntity.ok(updatedCargo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 貨物の追跡履歴取得
     * 
     * @param cargoId 貨物ID
     * @return 指定された貨物の追跡履歴リスト
     */
    @GetMapping("/{cargoId}/tracking")
    public ResponseEntity<List<Tracking>> getTrackingHistory(@PathVariable String cargoId) {
        List<Tracking> trackingHistory = cargoService.getTrackingHistory(cargoId);
        return ResponseEntity.ok(trackingHistory);
    }
} 