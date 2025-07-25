package com.aircargo.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * 入荷エンティティのテストクラス
 * 
 * Inboundエンティティのプロパティ、コンストラクタ、Lombokアノテーションの動作をテストします。
 */
class InboundTest {

    private Inbound inbound;
    private LocalDateTime testDateTime;
    private LocalDate testDate;
    private LocalTime testTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.now();
        testDate = LocalDate.now();
        testTime = LocalTime.now();
        inbound = new Inbound();
    }

    /**
     * 入荷エンティティの基本プロパティ設定テスト
     */
    @Test
    void testInboundProperties() {
        // テストデータの設定
        String inboundId = "IN12345678";
        String cargoId = "C12345678";
        String flightNumber = "NH001";
        LocalDate arrivalDate = testDate;
        LocalTime arrivalTime = testTime;
        String terminal = "T1";
        String status = "ARRIVED";
        String handlerId = "H001";
        String notes = "テスト入荷記録";

        // プロパティの設定
        inbound.setInboundId(inboundId);
        inbound.setCargoId(cargoId);
        inbound.setFlightNumber(flightNumber);
        inbound.setArrivalDate(arrivalDate);
        inbound.setArrivalTime(arrivalTime);
        inbound.setTerminal(terminal);
        inbound.setStatus(status);
        inbound.setHandlerId(handlerId);
        inbound.setNotes(notes);
        inbound.setCreatedDate(testDateTime);
        inbound.setVersion(1);

        // プロパティの検証
        assertEquals(inboundId, inbound.getInboundId());
        assertEquals(cargoId, inbound.getCargoId());
        assertEquals(flightNumber, inbound.getFlightNumber());
        assertEquals(arrivalDate, inbound.getArrivalDate());
        assertEquals(arrivalTime, inbound.getArrivalTime());
        assertEquals(terminal, inbound.getTerminal());
        assertEquals(status, inbound.getStatus());
        assertEquals(handlerId, inbound.getHandlerId());
        assertEquals(notes, inbound.getNotes());
        assertEquals(testDateTime, inbound.getCreatedDate());
        assertEquals(1, inbound.getVersion());
    }

    /**
     * 全引数コンストラクタのテスト
     */
    @Test
    void testAllArgsConstructor() {
        String inboundId = "IN12345678";
        String cargoId = "C12345678";
        String flightNumber = "NH001";
        LocalDate arrivalDate = testDate;
        LocalTime arrivalTime = testTime;
        String terminal = "T1";
        String status = "ARRIVED";
        String handlerId = "H001";
        String notes = "テスト入荷記録";

        Inbound inboundWithConstructor = new Inbound(
            inboundId, cargoId, flightNumber, arrivalDate, arrivalTime,
            terminal, status, handlerId, notes, testDateTime, 1
        );

        // コンストラクタで設定された値の検証
        assertEquals(inboundId, inboundWithConstructor.getInboundId());
        assertEquals(cargoId, inboundWithConstructor.getCargoId());
        assertEquals(flightNumber, inboundWithConstructor.getFlightNumber());
        assertEquals(arrivalDate, inboundWithConstructor.getArrivalDate());
        assertEquals(arrivalTime, inboundWithConstructor.getArrivalTime());
        assertEquals(terminal, inboundWithConstructor.getTerminal());
        assertEquals(status, inboundWithConstructor.getStatus());
        assertEquals(handlerId, inboundWithConstructor.getHandlerId());
        assertEquals(notes, inboundWithConstructor.getNotes());
        assertEquals(testDateTime, inboundWithConstructor.getCreatedDate());
        assertEquals(1, inboundWithConstructor.getVersion());
    }

    /**
     * 無引数コンストラクタのテスト
     */
    @Test
    void testNoArgsConstructor() {
        Inbound emptyInbound = new Inbound();
        assertNotNull(emptyInbound);
        // 無引数コンストラクタで作成されたオブジェクトがnullでないことを確認
    }

    /**
     * equalsとhashCodeのテスト
     */
    @Test
    void testEqualsAndHashCode() {
        Inbound inbound1 = new Inbound();
        Inbound inbound2 = new Inbound();
        
        inbound1.setInboundId("IN12345678");
        inbound2.setInboundId("IN12345678");

        // 同じIDを持つ入荷記録は等しいと判定される
        assertEquals(inbound1, inbound2);
        assertEquals(inbound1.hashCode(), inbound2.hashCode());
    }

    /**
     * toStringメソッドのテスト
     */
    @Test
    void testToString() {
        inbound.setInboundId("IN12345678");
        inbound.setFlightNumber("NH001");
        
        String toString = inbound.toString();
        
        // toStringに入荷IDとフライト番号が含まれていることを確認
        assertTrue(toString.contains("IN12345678"));
        assertTrue(toString.contains("NH001"));
    }
} 