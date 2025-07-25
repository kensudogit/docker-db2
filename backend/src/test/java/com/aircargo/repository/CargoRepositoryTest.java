package com.aircargo.repository;

import com.aircargo.entity.Cargo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 貨物リポジトリのテストクラス
 * 
 * CargoRepositoryのデータアクセスメソッドをテストします。
 * H2インメモリデータベースを使用して実際のデータベース操作をシミュレートします。
 */
@DataJpaTest
@ActiveProfiles("test")
class CargoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CargoRepository cargoRepository;

    private Cargo testCargo1;
    private Cargo testCargo2;

    @BeforeEach
    void setUp() {
        // テスト用の貨物データを作成
        testCargo1 = new Cargo();
        testCargo1.setCargoId("C12345678");
        testCargo1.setFlightNumber("NH001");
        testCargo1.setOriginAirport("NRT");
        testCargo1.setDestinationAirport("LAX");
        testCargo1.setCargoType("一般貨物");
        testCargo1.setWeight(new BigDecimal("100.50"));
        testCargo1.setVolume(new BigDecimal("2.5"));
        testCargo1.setStatus("PENDING");
        testCargo1.setShipperName("田中太郎");
        testCargo1.setConsigneeName("佐藤花子");
        testCargo1.setCreatedDate(LocalDateTime.now());
        testCargo1.setUpdatedDate(LocalDateTime.now());
        testCargo1.setVersion(1);

        testCargo2 = new Cargo();
        testCargo2.setCargoId("C87654321");
        testCargo2.setFlightNumber("NH001");
        testCargo2.setOriginAirport("NRT");
        testCargo2.setDestinationAirport("LAX");
        testCargo2.setCargoType("危険物");
        testCargo2.setWeight(new BigDecimal("50.25"));
        testCargo2.setVolume(new BigDecimal("1.0"));
        testCargo2.setStatus("IN_TRANSIT");
        testCargo2.setShipperName("山田次郎");
        testCargo2.setConsigneeName("鈴木一郎");
        testCargo2.setCreatedDate(LocalDateTime.now());
        testCargo2.setUpdatedDate(LocalDateTime.now());
        testCargo2.setVersion(1);

        // データベースにテストデータを保存
        entityManager.persistAndFlush(testCargo1);
        entityManager.persistAndFlush(testCargo2);
    }

    /**
     * 全貨物取得のテスト
     */
    @Test
    void testFindAll() {
        // メソッドの実行
        List<Cargo> result = cargoRepository.findAll();

        // 結果の検証
        assertNotNull(result);
        assertTrue(result.size() >= 2);
        
        // 保存したテストデータが含まれていることを確認
        boolean foundCargo1 = result.stream()
                .anyMatch(cargo -> "C12345678".equals(cargo.getCargoId()));
        boolean foundCargo2 = result.stream()
                .anyMatch(cargo -> "C87654321".equals(cargo.getCargoId()));
        
        assertTrue(foundCargo1);
        assertTrue(foundCargo2);
    }

    /**
     * 貨物IDによる貨物取得のテスト
     */
    @Test
    void testFindById() {
        // メソッドの実行
        Optional<Cargo> result = cargoRepository.findById("C12345678");

        // 結果の検証
        assertTrue(result.isPresent());
        assertEquals("C12345678", result.get().getCargoId());
        assertEquals("NH001", result.get().getFlightNumber());
        assertEquals("田中太郎", result.get().getShipperName());
    }

    /**
     * 存在しない貨物IDでの取得テスト
     */
    @Test
    void testFindByIdNotFound() {
        // メソッドの実行
        Optional<Cargo> result = cargoRepository.findById("NONEXISTENT");

        // 結果の検証
        assertFalse(result.isPresent());
    }

    /**
     * フライト番号による貨物検索のテスト
     */
    @Test
    void testFindByFlightNumber() {
        // メソッドの実行
        List<Cargo> result = cargoRepository.findByFlightNumber("NH001");

        // 結果の検証
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // すべての結果が同じフライト番号を持つことを確認
        result.forEach(cargo -> assertEquals("NH001", cargo.getFlightNumber()));
    }

    /**
     * 存在しないフライト番号での検索テスト
     */
    @Test
    void testFindByFlightNumberNotFound() {
        // メソッドの実行
        List<Cargo> result = cargoRepository.findByFlightNumber("NH999");

        // 結果の検証
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * ステータスによる貨物検索のテスト
     */
    @Test
    void testFindByStatus() {
        // メソッドの実行
        List<Cargo> result = cargoRepository.findByStatus("PENDING");

        // 結果の検証
        assertNotNull(result);
        assertTrue(result.size() >= 1);
        
        // すべての結果が同じステータスを持つことを確認
        result.forEach(cargo -> assertEquals("PENDING", cargo.getStatus()));
    }

    /**
     * 荷送人名による貨物検索のテスト
     */
    @Test
    void testFindByShipperNameContainingIgnoreCase() {
        // メソッドの実行
        List<Cargo> result = cargoRepository.findByShipperNameContainingIgnoreCase("田中");

        // 結果の検証
        assertNotNull(result);
        assertTrue(result.size() >= 1);
        
        // 結果に「田中」が含まれていることを確認
        result.forEach(cargo -> assertTrue(cargo.getShipperName().contains("田中")));
    }

    /**
     * 荷送人名による貨物検索（大文字小文字を区別しない）のテスト
     */
    @Test
    void testFindByShipperNameContainingIgnoreCaseCaseInsensitive() {
        // メソッドの実行
        List<Cargo> result = cargoRepository.findByShipperNameContainingIgnoreCase("たなか");

        // 結果の検証
        assertNotNull(result);
        assertTrue(result.size() >= 1);
        
        // 大文字小文字を区別せずに検索できることを確認
        result.forEach(cargo -> assertTrue(cargo.getShipperName().contains("田中")));
    }

    /**
     * 荷受人名による貨物検索のテスト
     */
    @Test
    void testFindByConsigneeNameContainingIgnoreCase() {
        // メソッドの実行
        List<Cargo> result = cargoRepository.findByConsigneeNameContainingIgnoreCase("佐藤");

        // 結果の検証
        assertNotNull(result);
        assertTrue(result.size() >= 1);
        
        // 結果に「佐藤」が含まれていることを確認
        result.forEach(cargo -> assertTrue(cargo.getConsigneeName().contains("佐藤")));
    }

    /**
     * 新規貨物保存のテスト
     */
    @Test
    void testSave() {
        // 新しい貨物データを作成
        Cargo newCargo = new Cargo();
        newCargo.setCargoId("C99999999");
        newCargo.setFlightNumber("JL001");
        newCargo.setOriginAirport("HND");
        newCargo.setDestinationAirport("JFK");
        newCargo.setCargoType("生鮮品");
        newCargo.setWeight(new BigDecimal("75.00"));
        newCargo.setVolume(new BigDecimal("3.0"));
        newCargo.setStatus("SCHEDULED");
        newCargo.setShipperName("高橋三郎");
        newCargo.setConsigneeName("伊藤四郎");
        newCargo.setCreatedDate(LocalDateTime.now());
        newCargo.setUpdatedDate(LocalDateTime.now());
        newCargo.setVersion(1);

        // メソッドの実行
        Cargo savedCargo = cargoRepository.save(newCargo);

        // 結果の検証
        assertNotNull(savedCargo);
        assertEquals("C99999999", savedCargo.getCargoId());
        assertEquals("JL001", savedCargo.getFlightNumber());
        assertEquals("高橋三郎", savedCargo.getShipperName());

        // データベースから取得して確認
        Optional<Cargo> retrievedCargo = cargoRepository.findById("C99999999");
        assertTrue(retrievedCargo.isPresent());
        assertEquals("JL001", retrievedCargo.get().getFlightNumber());
    }

    /**
     * 貨物削除のテスト
     */
    @Test
    void testDelete() {
        // 削除前の確認
        Optional<Cargo> beforeDelete = cargoRepository.findById("C12345678");
        assertTrue(beforeDelete.isPresent());

        // メソッドの実行
        cargoRepository.deleteById("C12345678");

        // 削除後の確認
        Optional<Cargo> afterDelete = cargoRepository.findById("C12345678");
        assertFalse(afterDelete.isPresent());
    }
} 