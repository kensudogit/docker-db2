package com.aircargo.controller;

import com.aircargo.entity.Cargo;
import com.aircargo.entity.Inbound;
import com.aircargo.entity.Outbound;
import com.aircargo.entity.Tracking;
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
@RequestMapping("/cargo")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "貨物管理", description = "貨物のCRUD操作")
@CrossOrigin(origins = "*")
public class CargoController {

    private final CargoService cargoService;

    @GetMapping
    @Operation(summary = "全貨物取得", description = "全ての貨物情報を取得します")
    public ResponseEntity<List<Cargo>> getAllCargo() {
        List<Cargo> cargoList = cargoService.getAllCargo();
        return ResponseEntity.ok(cargoList);
    }

    @GetMapping("/{cargoId}")
    @Operation(summary = "貨物詳細取得", description = "指定された貨物IDの詳細情報を取得します")
    public ResponseEntity<Cargo> getCargoById(@PathVariable String cargoId) {
        Optional<Cargo> cargo = cargoService.getCargoById(cargoId);
        return cargo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "貨物作成", description = "新しい貨物を登録します")
    public ResponseEntity<Cargo> createCargo(@Valid @RequestBody Cargo cargo) {
        Cargo createdCargo = cargoService.createCargo(cargo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCargo);
    }

    @PutMapping("/{cargoId}")
    @Operation(summary = "貨物更新", description = "指定された貨物IDの情報を更新します")
    public ResponseEntity<Cargo> updateCargo(@PathVariable String cargoId, @Valid @RequestBody Cargo cargo) {
        try {
            Cargo updatedCargo = cargoService.updateCargo(cargoId, cargo);
            return ResponseEntity.ok(updatedCargo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{cargoId}")
    @Operation(summary = "貨物削除", description = "指定された貨物IDの貨物を削除します")
    public ResponseEntity<Void> deleteCargo(@PathVariable String cargoId) {
        cargoService.deleteCargo(cargoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "ステータス別貨物取得", description = "指定されたステータスの貨物一覧を取得します")
    public ResponseEntity<List<Cargo>> getCargoByStatus(@PathVariable Cargo.CargoStatus status) {
        List<Cargo> cargoList = cargoService.getCargoByStatus(status);
        return ResponseEntity.ok(cargoList);
    }

    @GetMapping("/flight/{flightNumber}")
    @Operation(summary = "フライト別貨物取得", description = "指定されたフライト番号の貨物一覧を取得します")
    public ResponseEntity<List<Cargo>> getCargoByFlightNumber(@PathVariable String flightNumber) {
        List<Cargo> cargoList = cargoService.getCargoByFlightNumber(flightNumber);
        return ResponseEntity.ok(cargoList);
    }

    @GetMapping("/search")
    @Operation(summary = "貨物検索", description = "キーワードで貨物を検索します")
    public ResponseEntity<List<Cargo>> searchCargo(@RequestParam String keyword) {
        List<Cargo> cargoList = cargoService.searchCargo(keyword);
        return ResponseEntity.ok(cargoList);
    }

    @GetMapping("/{cargoId}/tracking")
    @Operation(summary = "貨物追跡情報取得", description = "指定された貨物IDの追跡情報を取得します")
    public ResponseEntity<List<Tracking>> getTrackingByCargoId(@PathVariable String cargoId) {
        List<Tracking> trackingList = cargoService.getTrackingByCargoId(cargoId);
        return ResponseEntity.ok(trackingList);
    }

    @PostMapping("/{cargoId}/tracking")
    @Operation(summary = "追跡情報作成", description = "指定された貨物IDの追跡情報を作成します")
    public ResponseEntity<Tracking> createTracking(@PathVariable String cargoId, @Valid @RequestBody Tracking tracking) {
        tracking.setCargoId(cargoId);
        Tracking createdTracking = cargoService.createTracking(tracking);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTracking);
    }

    @GetMapping("/stats/status/{status}")
    @Operation(summary = "ステータス別統計", description = "指定されたステータスの貨物数を取得します")
    public ResponseEntity<Long> getCargoCountByStatus(@PathVariable Cargo.CargoStatus status) {
        long count = cargoService.getCargoCountByStatus(status);
        return ResponseEntity.ok(count);
    }
} 