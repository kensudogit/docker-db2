package com.aircargo.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * 出荷エンティティのテストクラス
 * 
 * Outboundエンティティのプロパティ、コンストラクタ、Lombokアノテーションの動作をテストします。
 */
class OutboundTest {

    private Outbound outbound;
    private LocalDateTime testDateTime;
    private LocalDate testDate;
    private LocalTime testTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.now();
        testDate = LocalDate.now();
        testTime = LocalTime.now();
        outbound = new Outbound();
    }

    /**
     * 出荷エンティティの基本プロパティ設定テスト
     */
    @Test
    void testOutboundProperties() {
        // テストデータの設定
        String outboundId = "OUT12345678";
        String cargoId = "C12345678";
        String flightNumber = "NH001";
        LocalDate departureDate = testDate;
        LocalTime departureTime = testTime;
        String terminal = "T1";
        String status = "SCHEDULED";
        String handlerId = "H001";
        String notes = "テスト出荷記録";

        // プロパティの設定
        outbound.setOutboundId(outboundId);
        outbound.setCargoId(cargoId);
        outbound.setFlightNumber(flightNumber);
        outbound.setDepartureDate(departureDate);
        outbound.setDepartureTime(departureTime);
        outbound.setTerminal(terminal);
        outbound.setStatus(status);
        outbound.setHandlerId(handlerId);
        outbound.setNotes(notes);
        outbound.setCreatedDate(testDateTime);
        outbound.setVersion(1);

        // プロパティの検証
        assertEquals(outboundId, outbound.getOutboundId());
        assertEquals(cargoId, outbound.getCargoId());
        assertEquals(flightNumber, outbound.getFlightNumber());
        assertEquals(departureDate, outbound.getDepartureDate());
        assertEquals(departureTime, outbound.getDepartureTime());
        assertEquals(terminal, outbound.getTerminal());
        assertEquals(status, outbound.getStatus());
        assertEquals(handlerId, outbound.getHandlerId());
        assertEquals(notes, outbound.getNotes());
        assertEquals(testDateTime, outbound.getCreatedDate());
        assertEquals(1, outbound.getVersion());
    }

    /**
     * 全引数コンストラクタのテスト
     */
    @Test
    void testAllArgsConstructor() {
        String outboundId = "OUT12345678";
        String cargoId = "C12345678";
        String flightNumber = "NH001";
        LocalDate departureDate = testDate;
        LocalTime departureTime = testTime;
        String terminal = "T1";
        String status = "SCHEDULED";
        String handlerId = "H001";
        String notes = "テスト出荷記録";

        Outbound outboundWithConstructor = new Outbound(
            outboundId, cargoId, flightNumber, departureDate, departureTime,
            terminal, status, handlerId, notes, testDateTime, 1
        );

        // コンストラクタで設定された値の検証
        assertEquals(outboundId, outboundWithConstructor.getOutboundId());
        assertEquals(cargoId, outboundWithConstructor.getCargoId());
        assertEquals(flightNumber, outboundWithConstructor.getFlightNumber());
        assertEquals(departureDate, outboundWithConstructor.getDepartureDate());
        assertEquals(departureTime, outboundWithConstructor.getDepartureTime());
        assertEquals(terminal, outboundWithConstructor.getTerminal());
        assertEquals(status, outboundWithConstructor.getStatus());
        assertEquals(handlerId, outboundWithConstructor.getHandlerId());
        assertEquals(notes, outboundWithConstructor.getNotes());
        assertEquals(testDateTime, outboundWithConstructor.getCreatedDate());
        assertEquals(1, outboundWithConstructor.getVersion());
    }

    /**
     * 無引数コンストラクタのテスト
     */
    @Test
    void testNoArgsConstructor() {
        Outbound emptyOutbound = new Outbound();
        assertNotNull(emptyOutbound);
        // 無引数コンストラクタで作成されたオブジェクトがnullでないことを確認
    }

    /**
     * equalsとhashCodeのテスト
     */
    @Test
    void testEqualsAndHashCode() {
        Outbound outbound1 = new Outbound();
        Outbound outbound2 = new Outbound();
        
        outbound1.setOutboundId("OUT12345678");
        outbound2.setOutboundId("OUT12345678");

        // 同じIDを持つ出荷記録は等しいと判定される
        assertEquals(outbound1, outbound2);
        assertEquals(outbound1.hashCode(), outbound2.hashCode());
    }

    /**
     * toStringメソッドのテスト
     */
    @Test
    void testToString() {
        outbound.setOutboundId("OUT12345678");
        outbound.setFlightNumber("NH001");
        
        String toString = outbound.toString();
        
        // toStringに出荷IDとフライト番号が含まれていることを確認
        assertTrue(toString.contains("OUT12345678"));
        assertTrue(toString.contains("NH001"));
    }
} 