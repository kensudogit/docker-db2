package com.aircargo.controller;

import com.aircargo.entity.Cargo;
import com.aircargo.entity.Tracking;
import com.aircargo.service.CargoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 貨物管理コントローラーのテストクラス
 * 
 * CargoControllerのRESTエンドポイントをテストします。
 * MockMvcを使用してHTTPリクエストとレスポンスをシミュレートします。
 */
@ExtendWith(MockitoExtension.class)
class CargoControllerTest {

    @Mock
    private CargoService cargoService;

    @InjectMocks
    private CargoController cargoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Cargo testCargo;
    private Tracking testTracking;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cargoController).build();
        objectMapper = new ObjectMapper();

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

        // テスト用の追跡データを作成
        testTracking = new Tracking();
        testTracking.setTrackingId("TR12345678");
        testTracking.setCargoId("C12345678");
        testTracking.setLocation("Narita International Airport");
        testTracking.setStatus("IN_TRANSIT");
        testTracking.setTimestamp(LocalDateTime.now());
    }

    /**
     * 全貨物取得エンドポイントのテスト
     */
    @Test
    void testGetAllCargos() throws Exception {
        // モックの設定
        when(cargoService.getAllCargos()).thenReturn(Arrays.asList(testCargo));

        // HTTPリクエストの実行と検証
        mockMvc.perform(get("/api/cargo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].cargoId").value("C12345678"))
                .andExpect(jsonPath("$[0].flightNumber").value("NH001"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(cargoService, times(1)).getAllCargos();
    }

    /**
     * 貨物IDによる貨物取得エンドポイントのテスト
     */
    @Test
    void testGetCargoById() throws Exception {
        // モックの設定
        when(cargoService.getCargoById("C12345678")).thenReturn(Optional.of(testCargo));

        // HTTPリクエストの実行と検証
        mockMvc.perform(get("/api/cargo/C12345678"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cargoId").value("C12345678"))
                .andExpect(jsonPath("$.flightNumber").value("NH001"));

        verify(cargoService, times(1)).getCargoById("C12345678");
    }

    /**
     * 存在しない貨物IDでの取得テスト
     */
    @Test
    void testGetCargoByIdNotFound() throws Exception {
        // モックの設定
        when(cargoService.getCargoById("NONEXISTENT")).thenReturn(Optional.empty());

        // HTTPリクエストの実行と検証
        mockMvc.perform(get("/api/cargo/NONEXISTENT"))
                .andExpect(status().isNotFound());

        verify(cargoService, times(1)).getCargoById("NONEXISTENT");
    }

    /**
     * 新規貨物作成エンドポイントのテスト
     */
    @Test
    void testCreateCargo() throws Exception {
        // モックの設定
        when(cargoService.createCargo(any(Cargo.class))).thenReturn(testCargo);

        // HTTPリクエストの実行と検証
        mockMvc.perform(post("/api/cargo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCargo)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cargoId").value("C12345678"))
                .andExpect(jsonPath("$.flightNumber").value("NH001"));

        verify(cargoService, times(1)).createCargo(any(Cargo.class));
    }

    /**
     * 貨物更新エンドポイントのテスト
     */
    @Test
    void testUpdateCargo() throws Exception {
        // モックの設定
        when(cargoService.updateCargo(eq("C12345678"), any(Cargo.class))).thenReturn(testCargo);

        // HTTPリクエストの実行と検証
        mockMvc.perform(put("/api/cargo/C12345678")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCargo)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cargoId").value("C12345678"));

        verify(cargoService, times(1)).updateCargo(eq("C12345678"), any(Cargo.class));
    }

    /**
     * 存在しない貨物の更新テスト
     */
    @Test
    void testUpdateCargoNotFound() throws Exception {
        // モックの設定
        when(cargoService.updateCargo(eq("NONEXISTENT"), any(Cargo.class)))
                .thenThrow(new RuntimeException("貨物が見つかりません"));

        // HTTPリクエストの実行と検証
        mockMvc.perform(put("/api/cargo/NONEXISTENT")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCargo)))
                .andExpect(status().isNotFound());

        verify(cargoService, times(1)).updateCargo(eq("NONEXISTENT"), any(Cargo.class));
    }

    /**
     * 貨物削除エンドポイントのテスト
     */
    @Test
    void testDeleteCargo() throws Exception {
        // モックの設定
        doNothing().when(cargoService).deleteCargo("C12345678");

        // HTTPリクエストの実行と検証
        mockMvc.perform(delete("/api/cargo/C12345678"))
                .andExpect(status().isOk());

        verify(cargoService, times(1)).deleteCargo("C12345678");
    }

    /**
     * フライト番号による貨物検索エンドポイントのテスト
     */
    @Test
    void testGetCargosByFlightNumber() throws Exception {
        // モックの設定
        when(cargoService.getCargosByFlightNumber("NH001")).thenReturn(Arrays.asList(testCargo));

        // HTTPリクエストの実行と検証
        mockMvc.perform(get("/api/cargo/flight/NH001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].flightNumber").value("NH001"));

        verify(cargoService, times(1)).getCargosByFlightNumber("NH001");
    }

    /**
     * ステータスによる貨物検索エンドポイントのテスト
     */
    @Test
    void testGetCargosByStatus() throws Exception {
        // モックの設定
        when(cargoService.getCargosByStatus("PENDING")).thenReturn(Arrays.asList(testCargo));

        // HTTPリクエストの実行と検証
        mockMvc.perform(get("/api/cargo/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(cargoService, times(1)).getCargosByStatus("PENDING");
    }

    /**
     * 荷送人名による貨物検索エンドポイントのテスト
     */
    @Test
    void testSearchCargosByShipperName() throws Exception {
        // モックの設定
        when(cargoService.searchCargosByShipperName("田中")).thenReturn(Arrays.asList(testCargo));

        // HTTPリクエストの実行と検証
        mockMvc.perform(get("/api/cargo/search/shipper")
                .param("shipperName", "田中"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].shipperName").value("田中太郎"));

        verify(cargoService, times(1)).searchCargosByShipperName("田中");
    }

    /**
     * 荷受人名による貨物検索エンドポイントのテスト
     */
    @Test
    void testSearchCargosByConsigneeName() throws Exception {
        // モックの設定
        when(cargoService.searchCargosByConsigneeName("佐藤")).thenReturn(Arrays.asList(testCargo));

        // HTTPリクエストの実行と検証
        mockMvc.perform(get("/api/cargo/search/consignee")
                .param("consigneeName", "佐藤"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].consigneeName").value("佐藤花子"));

        verify(cargoService, times(1)).searchCargosByConsigneeName("佐藤");
    }

    /**
     * 貨物ステータス更新エンドポイントのテスト
     */
    @Test
    void testUpdateCargoStatus() throws Exception {
        // モックの設定
        when(cargoService.updateCargoStatus("C12345678", "IN_TRANSIT")).thenReturn(testCargo);

        // HTTPリクエストの実行と検証
        mockMvc.perform(put("/api/cargo/C12345678/status")
                .param("status", "IN_TRANSIT"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cargoId").value("C12345678"));

        verify(cargoService, times(1)).updateCargoStatus("C12345678", "IN_TRANSIT");
    }

    /**
     * 追跡履歴取得エンドポイントのテスト
     */
    @Test
    void testGetTrackingHistory() throws Exception {
        // モックの設定
        when(cargoService.getTrackingHistory("C12345678")).thenReturn(Arrays.asList(testTracking));

        // HTTPリクエストの実行と検証
        mockMvc.perform(get("/api/cargo/C12345678/tracking"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].trackingId").value("TR12345678"))
                .andExpect(jsonPath("$[0].cargoId").value("C12345678"));

        verify(cargoService, times(1)).getTrackingHistory("C12345678");
    }
} 