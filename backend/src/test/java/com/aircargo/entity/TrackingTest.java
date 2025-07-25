package com.aircargo.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * 追跡エンティティのテストクラス
 * 
 * Trackingエンティティのプロパティ、コンストラクタ、Lombokアノテーションの動作をテストします。
 */
class TrackingTest {

    private Tracking tracking;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.now();
        tracking = new Tracking();
    }

    /**
     * 追跡エンティティの基本プロパティ設定テスト
     */
    @Test
    void testTrackingProperties() {
        // テストデータの設定
        String trackingId = "TR12345678";
        String cargoId = "C12345678";
        String location = "Narita International Airport";
        String status = "IN_TRANSIT";
        LocalDateTime timestamp = testDateTime;
        String handlerId = "H001";
        String notes = "テスト追跡記録";

        // プロパティの設定
        tracking.setTrackingId(trackingId);
        tracking.setCargoId(cargoId);
        tracking.setLocation(location);
        tracking.setStatus(status);
        tracking.setTimestamp(timestamp);
        tracking.setHandlerId(handlerId);
        tracking.setNotes(notes);
        tracking.setVersion(1);

        // プロパティの検証
        assertEquals(trackingId, tracking.getTrackingId());
        assertEquals(cargoId, tracking.getCargoId());
        assertEquals(location, tracking.getLocation());
        assertEquals(status, tracking.getStatus());
        assertEquals(timestamp, tracking.getTimestamp());
        assertEquals(handlerId, tracking.getHandlerId());
        assertEquals(notes, tracking.getNotes());
        assertEquals(1, tracking.getVersion());
    }

    /**
     * 全引数コンストラクタのテスト
     */
    @Test
    void testAllArgsConstructor() {
        String trackingId = "TR12345678";
        String cargoId = "C12345678";
        String location = "Narita International Airport";
        String status = "IN_TRANSIT";
        LocalDateTime timestamp = testDateTime;
        String handlerId = "H001";
        String notes = "テスト追跡記録";

        Tracking trackingWithConstructor = new Tracking(
            trackingId, cargoId, location, status, timestamp, handlerId, notes, 1
        );

        // コンストラクタで設定された値の検証
        assertEquals(trackingId, trackingWithConstructor.getTrackingId());
        assertEquals(cargoId, trackingWithConstructor.getCargoId());
        assertEquals(location, trackingWithConstructor.getLocation());
        assertEquals(status, trackingWithConstructor.getStatus());
        assertEquals(timestamp, trackingWithConstructor.getTimestamp());
        assertEquals(handlerId, trackingWithConstructor.getHandlerId());
        assertEquals(notes, trackingWithConstructor.getNotes());
        assertEquals(1, trackingWithConstructor.getVersion());
    }

    /**
     * 無引数コンストラクタのテスト
     */
    @Test
    void testNoArgsConstructor() {
        Tracking emptyTracking = new Tracking();
        assertNotNull(emptyTracking);
        // 無引数コンストラクタで作成されたオブジェクトがnullでないことを確認
    }

    /**
     * equalsとhashCodeのテスト
     */
    @Test
    void testEqualsAndHashCode() {
        Tracking tracking1 = new Tracking();
        Tracking tracking2 = new Tracking();
        
        tracking1.setTrackingId("TR12345678");
        tracking2.setTrackingId("TR12345678");

        // 同じIDを持つ追跡記録は等しいと判定される
        assertEquals(tracking1, tracking2);
        assertEquals(tracking1.hashCode(), tracking2.hashCode());
    }

    /**
     * toStringメソッドのテスト
     */
    @Test
    void testToString() {
        tracking.setTrackingId("TR12345678");
        tracking.setLocation("Narita International Airport");
        
        String toString = tracking.toString();
        
        // toStringに追跡IDと位置情報が含まれていることを確認
        assertTrue(toString.contains("TR12345678"));
        assertTrue(toString.contains("Narita International Airport"));
    }
} 