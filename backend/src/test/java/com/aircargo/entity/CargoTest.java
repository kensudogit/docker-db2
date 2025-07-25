package com.aircargo.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 貨物エンティティのテストクラス
 * 
 * Cargoエンティティのプロパティ、コンストラクタ、Lombokアノテーションの動作をテストします。
 */
class CargoTest {

    private Cargo cargo;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.now();
        cargo = new Cargo();
    }

    /**
     * 貨物エンティティの基本プロパティ設定テスト
     */
    @Test
    void testCargoProperties() {
        // テストデータの設定
        String cargoId = "C12345678";
        String flightNumber = "NH001";
        String originAirport = "NRT";
        String destinationAirport = "LAX";
        String cargoType = "一般貨物";
        BigDecimal weight = new BigDecimal("100.50");
        BigDecimal volume = new BigDecimal("2.5");
        String status = "PENDING";
        String shipperName = "田中太郎";
        String consigneeName = "佐藤花子";

        // プロパティの設定
        cargo.setCargoId(cargoId);
        cargo.setFlightNumber(flightNumber);
        cargo.setOriginAirport(originAirport);
        cargo.setDestinationAirport(destinationAirport);
        cargo.setCargoType(cargoType);
        cargo.setWeight(weight);
        cargo.setVolume(volume);
        cargo.setStatus(status);
        cargo.setShipperName(shipperName);
        cargo.setConsigneeName(consigneeName);
        cargo.setCreatedDate(testDateTime);
        cargo.setUpdatedDate(testDateTime);
        cargo.setVersion(1);

        // プロパティの検証
        assertEquals(cargoId, cargo.getCargoId());
        assertEquals(flightNumber, cargo.getFlightNumber());
        assertEquals(originAirport, cargo.getOriginAirport());
        assertEquals(destinationAirport, cargo.getDestinationAirport());
        assertEquals(cargoType, cargo.getCargoType());
        assertEquals(weight, cargo.getWeight());
        assertEquals(volume, cargo.getVolume());
        assertEquals(status, cargo.getStatus());
        assertEquals(shipperName, cargo.getShipperName());
        assertEquals(consigneeName, cargo.getConsigneeName());
        assertEquals(testDateTime, cargo.getCreatedDate());
        assertEquals(testDateTime, cargo.getUpdatedDate());
        assertEquals(1, cargo.getVersion());
    }

    /**
     * 全引数コンストラクタのテスト
     */
    @Test
    void testAllArgsConstructor() {
        String cargoId = "C12345678";
        String flightNumber = "NH001";
        String originAirport = "NRT";
        String destinationAirport = "LAX";
        String cargoType = "一般貨物";
        BigDecimal weight = new BigDecimal("100.50");
        BigDecimal volume = new BigDecimal("2.5");
        String status = "PENDING";
        String shipperName = "田中太郎";
        String consigneeName = "佐藤花子";

        Cargo cargoWithConstructor = new Cargo(
            cargoId, flightNumber, originAirport, destinationAirport,
            cargoType, weight, volume, status, shipperName, consigneeName,
            testDateTime, testDateTime, 1
        );

        // コンストラクタで設定された値の検証
        assertEquals(cargoId, cargoWithConstructor.getCargoId());
        assertEquals(flightNumber, cargoWithConstructor.getFlightNumber());
        assertEquals(originAirport, cargoWithConstructor.getOriginAirport());
        assertEquals(destinationAirport, cargoWithConstructor.getDestinationAirport());
        assertEquals(cargoType, cargoWithConstructor.getCargoType());
        assertEquals(weight, cargoWithConstructor.getWeight());
        assertEquals(volume, cargoWithConstructor.getVolume());
        assertEquals(status, cargoWithConstructor.getStatus());
        assertEquals(shipperName, cargoWithConstructor.getShipperName());
        assertEquals(consigneeName, cargoWithConstructor.getConsigneeName());
        assertEquals(testDateTime, cargoWithConstructor.getCreatedDate());
        assertEquals(testDateTime, cargoWithConstructor.getUpdatedDate());
        assertEquals(1, cargoWithConstructor.getVersion());
    }

    /**
     * 無引数コンストラクタのテスト
     */
    @Test
    void testNoArgsConstructor() {
        Cargo emptyCargo = new Cargo();
        assertNotNull(emptyCargo);
        // 無引数コンストラクタで作成されたオブジェクトがnullでないことを確認
    }

    /**
     * equalsとhashCodeのテスト
     */
    @Test
    void testEqualsAndHashCode() {
        Cargo cargo1 = new Cargo();
        Cargo cargo2 = new Cargo();
        
        cargo1.setCargoId("C12345678");
        cargo2.setCargoId("C12345678");

        // 同じIDを持つ貨物は等しいと判定される
        assertEquals(cargo1, cargo2);
        assertEquals(cargo1.hashCode(), cargo2.hashCode());
    }

    /**
     * toStringメソッドのテスト
     */
    @Test
    void testToString() {
        cargo.setCargoId("C12345678");
        cargo.setFlightNumber("NH001");
        
        String toString = cargo.toString();
        
        // toStringに貨物IDとフライト番号が含まれていることを確認
        assertTrue(toString.contains("C12345678"));
        assertTrue(toString.contains("NH001"));
    }
} 