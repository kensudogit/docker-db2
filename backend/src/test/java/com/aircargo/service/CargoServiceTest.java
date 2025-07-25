package com.aircargo.service;

import com.aircargo.entity.Cargo;
import com.aircargo.entity.Inbound;
import com.aircargo.entity.Outbound;
import com.aircargo.entity.Tracking;
import com.aircargo.repository.CargoRepository;
import com.aircargo.repository.InboundRepository;
import com.aircargo.repository.OutboundRepository;
import com.aircargo.repository.TrackingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 貨物管理サービスのテストクラス
 * 
 * CargoServiceのビジネスロジックをテストします。
 * リポジトリの依存関係をモック化して、サービスクラスの動作を検証します。
 */
@ExtendWith(MockitoExtension.class)
class CargoServiceTest {

    @Mock
    private CargoRepository cargoRepository;

    @Mock
    private InboundRepository inboundRepository;

    @Mock
    private OutboundRepository outboundRepository;

    @Mock
    private TrackingRepository trackingRepository;

    @InjectMocks
    private CargoService cargoService;

    private Cargo testCargo;
    private Inbound testInbound;
    private Outbound testOutbound;
    private Tracking testTracking;

    @BeforeEach
    void setUp() {
        // テスト用の貨物データを作成
        testCargo = new Cargo();
        testCargo.setCargoId("C12345678");
        testCargo.setFlightNumber("NH001");
        testCargo.setOriginAirport("NRT");
        testCargo.setDestinationAirport("LAX");
        testCargo.setCargoType("一般貨物");
        testCargo.setWeight(new BigDecimal("100.50"));
        testCargo.setVolume(new BigDecimal("2.5"));
        testCargo.setStatus("PENDING");
        testCargo.setShipperName("田中太郎");
        testCargo.setConsigneeName("佐藤花子");
        testCargo.setCreatedDate(LocalDateTime.now());
        testCargo.setUpdatedDate(LocalDateTime.now());
        testCargo.setVersion(1);

        // テスト用の入荷データを作成
        testInbound = new Inbound();
        testInbound.setInboundId("IN12345678");
        testInbound.setCargoId("C12345678");
        testInbound.setFlightNumber("NH001");
        testInbound.setStatus("ARRIVED");

        // テスト用の出荷データを作成
        testOutbound = new Outbound();
        testOutbound.setOutboundId("OUT12345678");
        testOutbound.setCargoId("C12345678");
        testOutbound.setFlightNumber("NH001");
        testOutbound.setStatus("SCHEDULED");

        // テスト用の追跡データを作成
        testTracking = new Tracking();
        testTracking.setTrackingId("TR12345678");
        testTracking.setCargoId("C12345678");
        testTracking.setLocation("Narita International Airport");
        testTracking.setStatus("IN_TRANSIT");
        testTracking.setTimestamp(LocalDateTime.now());
    }

    /**
     * 全貨物取得のテスト
     */
    @Test
    void testGetAllCargos() {
        // モックの設定
        when(cargoRepository.findAll()).thenReturn(Arrays.asList(testCargo));

        // メソッドの実行
        List<Cargo> result = cargoService.getAllCargos();

        // 結果の検証
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCargo.getCargoId(), result.get(0).getCargoId());
        verify(cargoRepository, times(1)).findAll();
    }

    /**
     * 貨物IDによる貨物取得のテスト
     */
    @Test
    void testGetCargoById() {
        // モックの設定
        when(cargoRepository.findById("C12345678")).thenReturn(Optional.of(testCargo));

        // メソッドの実行
        Optional<Cargo> result = cargoService.getCargoById("C12345678");

        // 結果の検証
        assertTrue(result.isPresent());
        assertEquals(testCargo.getCargoId(), result.get().getCargoId());
        verify(cargoRepository, times(1)).findById("C12345678");
    }

    /**
     * 存在しない貨物IDでの取得テスト
     */
    @Test
    void testGetCargoByIdNotFound() {
        // モックの設定
        when(cargoRepository.findById("NONEXISTENT")).thenReturn(Optional.empty());

        // メソッドの実行
        Optional<Cargo> result = cargoService.getCargoById("NONEXISTENT");

        // 結果の検証
        assertFalse(result.isPresent());
        verify(cargoRepository, times(1)).findById("NONEXISTENT");
    }

    /**
     * フライト番号による貨物検索のテスト
     */
    @Test
    void testGetCargosByFlightNumber() {
        // モックの設定
        when(cargoRepository.findByFlightNumber("NH001")).thenReturn(Arrays.asList(testCargo));

        // メソッドの実行
        List<Cargo> result = cargoService.getCargosByFlightNumber("NH001");

        // 結果の検証
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("NH001", result.get(0).getFlightNumber());
        verify(cargoRepository, times(1)).findByFlightNumber("NH001");
    }

    /**
     * ステータスによる貨物検索のテスト
     */
    @Test
    void testGetCargosByStatus() {
        // モックの設定
        when(cargoRepository.findByStatus("PENDING")).thenReturn(Arrays.asList(testCargo));

        // メソッドの実行
        List<Cargo> result = cargoService.getCargosByStatus("PENDING");

        // 結果の検証
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        verify(cargoRepository, times(1)).findByStatus("PENDING");
    }

    /**
     * 新規貨物作成のテスト
     */
    @Test
    void testCreateCargo() {
        // モックの設定
        when(cargoRepository.save(any(Cargo.class))).thenReturn(testCargo);

        // メソッドの実行
        Cargo result = cargoService.createCargo(testCargo);

        // 結果の検証
        assertNotNull(result);
        assertNotNull(result.getCargoId());
        assertEquals("PENDING", result.getStatus());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getUpdatedDate());
        assertEquals(1, result.getVersion());
        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }

    /**
     * 貨物更新のテスト
     */
    @Test
    void testUpdateCargo() {
        // モックの設定
        when(cargoRepository.findById("C12345678")).thenReturn(Optional.of(testCargo));
        when(cargoRepository.save(any(Cargo.class))).thenReturn(testCargo);

        Cargo updateData = new Cargo();
        updateData.setFlightNumber("NH002");
        updateData.setStatus("IN_TRANSIT");

        // メソッドの実行
        Cargo result = cargoService.updateCargo("C12345678", updateData);

        // 結果の検証
        assertNotNull(result);
        verify(cargoRepository, times(1)).findById("C12345678");
        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }

    /**
     * 存在しない貨物の更新テスト
     */
    @Test
    void testUpdateCargoNotFound() {
        // モックの設定
        when(cargoRepository.findById("NONEXISTENT")).thenReturn(Optional.empty());

        Cargo updateData = new Cargo();

        // 例外が発生することを確認
        assertThrows(RuntimeException.class, () -> {
            cargoService.updateCargo("NONEXISTENT", updateData);
        });

        verify(cargoRepository, times(1)).findById("NONEXISTENT");
        verify(cargoRepository, never()).save(any(Cargo.class));
    }

    /**
     * 貨物削除のテスト
     */
    @Test
    void testDeleteCargo() {
        // モックの設定
        when(cargoRepository.findById("C12345678")).thenReturn(Optional.of(testCargo));
        doNothing().when(cargoRepository).delete(testCargo);

        // メソッドの実行
        cargoService.deleteCargo("C12345678");

        // 検証
        verify(cargoRepository, times(1)).findById("C12345678");
        verify(cargoRepository, times(1)).delete(testCargo);
    }

    /**
     * 貨物ステータス更新のテスト
     */
    @Test
    void testUpdateCargoStatus() {
        // モックの設定
        when(cargoRepository.findById("C12345678")).thenReturn(Optional.of(testCargo));
        when(cargoRepository.save(any(Cargo.class))).thenReturn(testCargo);

        // メソッドの実行
        Cargo result = cargoService.updateCargoStatus("C12345678", "IN_TRANSIT");

        // 結果の検証
        assertNotNull(result);
        verify(cargoRepository, times(1)).findById("C12345678");
        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }

    /**
     * 全入荷記録取得のテスト
     */
    @Test
    void testGetAllInbounds() {
        // モックの設定
        when(inboundRepository.findAll()).thenReturn(Arrays.asList(testInbound));

        // メソッドの実行
        List<Inbound> result = cargoService.getAllInbounds();

        // 結果の検証
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testInbound.getInboundId(), result.get(0).getInboundId());
        verify(inboundRepository, times(1)).findAll();
    }

    /**
     * 全出荷記録取得のテスト
     */
    @Test
    void testGetAllOutbounds() {
        // モックの設定
        when(outboundRepository.findAll()).thenReturn(Arrays.asList(testOutbound));

        // メソッドの実行
        List<Outbound> result = cargoService.getAllOutbounds();

        // 結果の検証
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testOutbound.getOutboundId(), result.get(0).getOutboundId());
        verify(outboundRepository, times(1)).findAll();
    }

    /**
     * 追跡履歴取得のテスト
     */
    @Test
    void testGetTrackingHistory() {
        // モックの設定
        when(trackingRepository.findByCargoIdOrderByTimestampDesc("C12345678"))
            .thenReturn(Arrays.asList(testTracking));

        // メソッドの実行
        List<Tracking> result = cargoService.getTrackingHistory("C12345678");

        // 結果の検証
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTracking.getTrackingId(), result.get(0).getTrackingId());
        verify(trackingRepository, times(1)).findByCargoIdOrderByTimestampDesc("C12345678");
    }
} 