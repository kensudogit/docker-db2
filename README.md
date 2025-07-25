# 航空貨物ロジステックシステム

## 概要
航空貨物の入出荷管理と追跡機能を提供するシステムです。

## 技術スタック
- **データベース**: IBM DB2
- **アプリケーションサーバ**: Spring Boot (組み込みTomcat)
- **バックエンド**: Java (Spring Boot) + Gradle + **Doma2**
- **フロントエンド**: React + Next.js
- **コンテナ**: Docker

## 機能
- 航空貨物の入荷管理
- 航空貨物の出荷管理
- 入出荷追跡機能
- 貨物状態監視
- レポート生成

## ディレクトリ構造
```
docker-db2/
├── backend/                 # Java Spring Boot アプリケーション (Gradle + Doma2)
│   ├── src/main/java/
│   │   ├── com/aircargo/
│   │   │   ├── entity/      # Doma2エンティティ
│   │   │   ├── dao/         # Doma2 DAOインターフェース
│   │   │   ├── service/     # ビジネスロジック
│   │   │   ├── controller/  # REST API
│   │   │   └── config/      # 設定クラス
│   │   └── resources/
│   │       └── META-INF/doma/ # Doma2 SQLファイル
├── frontend/               # React Next.js アプリケーション
├── database/               # DB2 データベーススクリプト
├── docker/                 # Docker 設定ファイル
└── docs/                   # ドキュメント
```

## セットアップ手順

### 1. 環境準備
```bash
# Docker と Docker Compose がインストールされていることを確認
docker --version
docker-compose --version
```

### 2. アプリケーション起動
```bash
# 全サービスを起動
docker-compose up -d

# 個別サービス起動
docker-compose up -d db2
docker-compose up -d backend
docker-compose up -d frontend
```

### 3. アクセス
- フロントエンド: http://localhost:3000
- バックエンド API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- DB2 管理コンソール: http://localhost:50000

## 開発ガイド

### バックエンド開発 (Gradle + Doma2)

#### ローカル開発環境
```bash
cd backend

# 依存関係のインストール
./gradlew build

# アプリケーションの起動
./gradlew bootRun

# テストの実行
./gradlew test

# JARファイルのビルド
./gradlew bootJar
```

#### Gradleコマンド
```bash
# プロジェクトのビルド
./gradlew build

# 依存関係の確認
./gradlew dependencies

# クリーンビルド
./gradlew clean build

# テストの実行
./gradlew test

# アプリケーションの起動
./gradlew bootRun
```

#### Doma2設定
- **エンティティ**: `src/main/java/com/aircargo/entity/`
- **DAO**: `src/main/java/com/aircargo/dao/`
- **SQLファイル**: `src/main/resources/META-INF/doma/`
- **設定**: `src/main/java/com/aircargo/config/DomaConfig.java`

#### Doma2の特徴
- **型安全なSQL**: コンパイル時にSQLの構文チェック
- **エンティティマッピング**: 自動的なオブジェクト-リレーション変換
- **バージョン管理**: 楽観的ロック機能
- **DB2対応**: DB2専用の方言サポート

#### 設定ファイル
- `build.gradle`: プロジェクトの依存関係とビルド設定（Doma2プラグイン含む）
- `settings.gradle`: プロジェクト名の設定
- `gradle.properties`: Gradleのプロパティ設定

### フロントエンド開発

#### ローカル開発環境
```bash
cd frontend

# 依存関係のインストール
npm install

# 開発サーバーの起動
npm run dev

# ビルド
npm run build

# 本番サーバーの起動
npm start
```

## データベース

### DB2接続情報
- **ホスト**: localhost (Docker内: db2)
- **ポート**: 50000
- **データベース**: AIRCARGO
- **ユーザー**: db2inst1
- **パスワード**: db2inst1

### データベース初期化
データベースの初期化は自動的に実行されます。手動で実行する場合：

```bash
# DB2コンテナに接続
docker exec -it air-cargo-db2 bash

# データベースに接続
db2 connect to AIRCARGO user db2inst1

# 初期化スクリプトの実行
db2 -f /var/custom/01_create_database.sql
```

## DB2 パフォーマンスチューニング

### 1. データベース設定の最適化

#### メモリ設定
```sql
-- バッファプールの設定
CREATE BUFFERPOOL BP32K IMMEDIATE SIZE 1000 PAGESIZE 32K;
CREATE BUFFERPOOL BP16K IMMEDIATE SIZE 2000 PAGESIZE 16K;
CREATE BUFFERPOOL BP8K IMMEDIATE SIZE 4000 PAGESIZE 8K;
CREATE BUFFERPOOL BP4K IMMEDIATE SIZE 8000 PAGESIZE 4K;

-- 共有メモリの設定
UPDATE DBM CFG USING SVCENAME DB2_db2inst1 IMMEDIATE;
UPDATE DBM CFG USING SHEAPTHRES 0 IMMEDIATE;
UPDATE DBM CFG USING INSTANCE_MEMORY AUTOMATIC IMMEDIATE;
```

#### データベース設定
```sql
-- データベース設定の最適化
UPDATE DB CFG FOR AIRCARGO USING LOGBUFSZ 1024 IMMEDIATE;
UPDATE DB CFG FOR AIRCARGO USING LOCKLIST 1000 IMMEDIATE;
UPDATE DB CFG FOR AIRCARGO USING MAXLOCKS 10 IMMEDIATE;
UPDATE DB CFG FOR AIRCARGO USING SORTHEAP 1000 IMMEDIATE;
UPDATE DB CFG FOR AIRCARGO USING STMTHEAP 8192 IMMEDIATE;
UPDATE DB CFG FOR AIRCARGO USING CATALOGCACHE_SZ 300 IMMEDIATE;
UPDATE DB CFG FOR AIRCARGO USING PCKCACHESZ 8192 IMMEDIATE;
```

### 2. インデックス戦略

#### 推奨インデックス
```sql
-- 貨物テーブルのインデックス
CREATE INDEX IDX_CARGO_FLIGHT_STATUS ON CARGO(FLIGHT_NUMBER, STATUS);
CREATE INDEX IDX_CARGO_SHIPPER ON CARGO(SHIPPER_NAME);
CREATE INDEX IDX_CARGO_CONSIGNEE ON CARGO(CONSIGNEE_NAME);
CREATE INDEX IDX_CARGO_CREATED_DATE ON CARGO(CREATED_DATE);
CREATE INDEX IDX_CARGO_ORIGIN_DEST ON CARGO(ORIGIN_AIRPORT, DESTINATION_AIRPORT);

-- 入荷テーブルのインデックス
CREATE INDEX IDX_INBOUND_CARGO_DATE ON INBOUND(CARGO_ID, ARRIVAL_DATE);
CREATE INDEX IDX_INBOUND_STATUS ON INBOUND(STATUS);
CREATE INDEX IDX_INBOUND_HANDLER ON INBOUND(HANDLER_ID);

-- 出荷テーブルのインデックス
CREATE INDEX IDX_OUTBOUND_CARGO_DATE ON OUTBOUND(CARGO_ID, DEPARTURE_DATE);
CREATE INDEX IDX_OUTBOUND_STATUS ON OUTBOUND(STATUS);
CREATE INDEX IDX_OUTBOUND_HANDLER ON OUTBOUND(HANDLER_ID);

-- 追跡テーブルのインデックス
CREATE INDEX IDX_TRACKING_CARGO_TIME ON TRACKING(CARGO_ID, TIMESTAMP);
CREATE INDEX IDX_TRACKING_STATUS ON TRACKING(STATUS);
CREATE INDEX IDX_TRACKING_LOCATION ON TRACKING(LOCATION);
```

#### 複合インデックスの最適化
```sql
-- 複合条件での検索を最適化
CREATE INDEX IDX_CARGO_COMPOSITE ON CARGO(STATUS, FLIGHT_NUMBER, CREATED_DATE);
CREATE INDEX IDX_INBOUND_COMPOSITE ON INBOUND(STATUS, ARRIVAL_DATE, CARGO_ID);
CREATE INDEX IDX_OUTBOUND_COMPOSITE ON OUTBOUND(STATUS, DEPARTURE_DATE, CARGO_ID);
```

### 3. SQLクエリの最適化

#### クエリパフォーマンスの改善
```sql
-- 統計情報の更新
RUNSTATS ON TABLE ALL TABLES WITH DISTRIBUTION AND DETAILED INDEXES ALL;

-- テーブル最適化
REORG TABLE CARGO ALLOW READ ACCESS;
REORG TABLE INBOUND ALLOW READ ACCESS;
REORG TABLE OUTBOUND ALLOW READ ACCESS;
REORG TABLE TRACKING ALLOW READ ACCESS;

-- インデックス最適化
REORG INDEXES ALL FOR TABLE CARGO ALLOW READ ACCESS;
REORG INDEXES ALL FOR TABLE INBOUND ALLOW READ ACCESS;
REORG INDEXES ALL FOR TABLE OUTBOUND ALLOW READ ACCESS;
REORG INDEXES ALL FOR TABLE TRACKING ALLOW READ ACCESS;
```

#### パーティショニング戦略
```sql
-- 日付ベースのパーティショニング（大量データの場合）
CREATE TABLE CARGO_PARTITIONED (
    CARGO_ID VARCHAR(20) NOT NULL,
    FLIGHT_NUMBER VARCHAR(10) NOT NULL,
    CREATED_DATE DATE NOT NULL,
    -- その他のカラム
) PARTITION BY RANGE (CREATED_DATE) (
    PARTITION P2024_01 STARTING FROM ('2024-01-01') ENDING AT ('2024-01-31'),
    PARTITION P2024_02 STARTING FROM ('2024-02-01') ENDING AT ('2024-02-29'),
    -- 月次パーティション
);
```

### 4. 接続プールとアプリケーション設定

#### Spring Boot設定
```yaml
# application.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      leak-detection-threshold: 60000
    db2:
      # DB2固有の設定
      isolation-level: READ_COMMITTED
      auto-commit: false
```

#### Doma2設定の最適化
```java
@Configuration
public class DomaConfig {
    
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:db2://localhost:50000/AIRCARGO");
        config.setUsername("db2inst1");
        config.setPassword("db2inst1");
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        
        // DB2固有の設定
        config.addDataSourceProperty("currentSchema", "DB2INST1");
        config.addDataSourceProperty("currentFunctionPath", "SYSIBM,SYSFUN,SYSPROC,DB2INST1");
        config.addDataSourceProperty("isolationLevel", "READ_COMMITTED");
        
        return new HikariDataSource(config);
    }
}
```

### 5. モニタリングとパフォーマンス監視

#### パフォーマンス監視クエリ
```sql
-- 現在の接続数
SELECT COUNT(*) FROM SYSIBMADM.SNAPAPPL_INFO;

-- バッファプールの使用状況
SELECT * FROM SYSIBMADM.SNAPBUFFERPOOL;

-- ロックの状況
SELECT * FROM SYSIBMADM.SNAPLOCK;

-- スロークエリの特定
SELECT * FROM SYSIBMADM.SNAPSTMT WHERE TOTAL_EXEC_TIME > 1000;

-- テーブルサイズの確認
SELECT TABSCHEMA, TABNAME, CARD, NPAGES, FPAGES 
FROM SYSCAT.TABLES 
WHERE TABSCHEMA = 'DB2INST1';

-- インデックス使用状況
SELECT * FROM SYSCAT.INDEXES WHERE TABSCHEMA = 'DB2INST1';
```

#### 定期メンテナンススクリプト
```bash
#!/bin/bash
# 定期メンテナンススクリプト

# DB2に接続
db2 connect to AIRCARGO user db2inst1

# 統計情報の更新
db2 "RUNSTATS ON TABLE ALL TABLES WITH DISTRIBUTION AND DETAILED INDEXES ALL"

# テーブルの最適化
db2 "REORG TABLE CARGO ALLOW READ ACCESS"
db2 "REORG TABLE INBOUND ALLOW READ ACCESS"
db2 "REORG TABLE OUTBOUND ALLOW READ ACCESS"
db2 "REORG TABLE TRACKING ALLOW READ ACCESS"

# インデックスの最適化
db2 "REORG INDEXES ALL FOR TABLE CARGO ALLOW READ ACCESS"
db2 "REORG INDEXES ALL FOR TABLE INBOUND ALLOW READ ACCESS"
db2 "REORG INDEXES ALL FOR TABLE OUTBOUND ALLOW READ ACCESS"
db2 "REORG INDEXES ALL FOR TABLE TRACKING ALLOW READ ACCESS"

# 接続を切断
db2 disconnect all
```

### 6. キャッシュ戦略

#### アプリケーションレベルキャッシュ
```java
@Service
@CacheConfig(cacheNames = "cargo")
public class CargoService {
    
    @Cacheable(key = "#cargoId")
    public Optional<Cargo> getCargoById(String cargoId) {
        return cargoRepository.findById(cargoId);
    }
    
    @Cacheable(key = "#flightNumber")
    public List<Cargo> getCargosByFlightNumber(String flightNumber) {
        return cargoRepository.findByFlightNumber(flightNumber);
    }
    
    @CacheEvict(allEntries = true)
    public Cargo createCargo(Cargo cargo) {
        return cargoRepository.save(cargo);
    }
}
```

#### データベースレベルキャッシュ
```sql
-- 頻繁にアクセスされるデータのキャッシュ
CREATE TABLE CACHE_FREQUENT_CARGO AS 
SELECT * FROM CARGO 
WHERE STATUS IN ('PENDING', 'IN_TRANSIT') 
WITH DATA;

-- 定期的なキャッシュ更新
CREATE EVENT CACHE_UPDATE_EVENT
ON SCHEDULE EVERY 1 HOUR
DO
  REFRESH TABLE CACHE_FREQUENT_CARGO;
```

### 7. バックアップとリカバリ戦略

#### バックアップ設定
```bash
# オンラインバックアップ
db2 backup database AIRCARGO online to /database/backup include logs

# 増分バックアップ
db2 backup database AIRCARGO incremental to /database/backup

# 差分バックアップ
db2 backup database AIRCARGO incremental delta to /database/backup
```

#### リカバリ手順
```bash
# データベースの復元
db2 restore database AIRCARGO from /database/backup taken at 20241201120000

# ロールフォワード
db2 rollforward database AIRCARGO to end of logs and stop
```

### 8. セキュリティとアクセス制御

#### ユーザー権限の管理
```sql
-- 読み取り専用ユーザーの作成
CREATE USER readonly_user PASSWORD 'password123';
GRANT CONNECT ON DATABASE TO USER readonly_user;
GRANT SELECT ON TABLE CARGO TO USER readonly_user;
GRANT SELECT ON TABLE INBOUND TO USER readonly_user;
GRANT SELECT ON TABLE OUTBOUND TO USER readonly_user;
GRANT SELECT ON TABLE TRACKING TO USER readonly_user;

-- アプリケーションユーザーの作成
CREATE USER app_user PASSWORD 'app_password123';
GRANT CONNECT ON DATABASE TO USER app_user;
GRANT ALL PRIVILEGES ON TABLE CARGO TO USER app_user;
GRANT ALL PRIVILEGES ON TABLE INBOUND TO USER app_user;
GRANT ALL PRIVILEGES ON TABLE OUTBOUND TO USER app_user;
GRANT ALL PRIVILEGES ON TABLE TRACKING TO USER app_user;
```

### 9. パフォーマンステスト

#### 負荷テストスクリプト
```java
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.hikari.maximum-pool-size=50",
    "spring.jpa.show-sql=false"
})
class PerformanceTest {
    
    @Test
    void testBulkInsertPerformance() {
        // 大量データ挿入のパフォーマンステスト
        List<Cargo> cargos = generateTestData(10000);
        
        long startTime = System.currentTimeMillis();
        cargoRepository.saveAll(cargos);
        long endTime = System.currentTimeMillis();
        
        assertTrue((endTime - startTime) < 30000); // 30秒以内
    }
    
    @Test
    void testQueryPerformance() {
        // クエリパフォーマンスのテスト
        long startTime = System.currentTimeMillis();
        List<Cargo> result = cargoRepository.findByStatus("PENDING");
        long endTime = System.currentTimeMillis();
        
        assertTrue((endTime - startTime) < 1000); // 1秒以内
    }
}
```

## API ドキュメント

### Swagger UI
APIドキュメントは以下のURLで確認できます：
```
http://localhost:8080/api/swagger-ui.html
```

### 主要なエンドポイント
- `GET /api/cargo` - 貨物一覧取得
- `POST /api/cargo` - 貨物作成
- `GET /api/cargo/{id}` - 貨物詳細取得
- `PUT /api/cargo/{id}` - 貨物更新
- `DELETE /api/cargo/{id}` - 貨物削除
- `GET /api/inbound` - 入荷一覧取得
- `POST /api/inbound` - 入荷作成
- `GET /api/outbound` - 出荷一覧取得
- `POST /api/outbound` - 出荷作成

## トラブルシューティング

### よくある問題

#### 1. ポート競合
```bash
# 使用中のポートを確認
netstat -tulpn | grep :8080
netstat -tulpn | grep :3000
netstat -tulpn | grep :50000
```

#### 2. データベース接続エラー
```bash
# DB2の状態確認
docker-compose logs db2

# データベース接続テスト
docker exec air-cargo-db2 db2 connect to AIRCARGO
```

#### 3. アプリケーション起動エラー
```bash
# Spring Bootアプリケーションのログ確認
docker-compose logs backend

# ヘルスチェック
curl http://localhost:8080/api/actuator/health
```

#### 4. Doma2関連エラー
```bash
# Doma2のビルド確認
./gradlew clean build

# SQLファイルの構文チェック
./gradlew domaCompile
```

#### 5. パフォーマンス問題
```bash
# DB2のパフォーマンス監視
docker exec air-cargo-db2 db2 "SELECT * FROM SYSIBMADM.SNAPAPPL_INFO"

# スロークエリの特定
docker exec air-cargo-db2 db2 "SELECT * FROM SYSIBMADM.SNAPSTMT WHERE TOTAL_EXEC_TIME > 1000"

# ロックの確認
docker exec air-cargo-db2 db2 "SELECT * FROM SYSIBMADM.SNAPLOCK"
```

#### 6. DB2コンテナの問題
```bash
# DB2コンテナの状態確認
docker-compose ps db2

# DB2コンテナのログ確認
docker-compose logs db2

# DB2コンテナの再起動
docker-compose restart db2

# DB2コンテナの完全リセット
docker-compose down
docker volume rm docker-db2_db2_data
docker-compose up -d db2
```

#### 7. DB2接続の問題
```bash
# DB2インスタンスの状態確認
docker exec -it air-cargo-db2 su - db2inst1 -c "db2start"

# DB2インスタンスの停止
docker exec -it air-cargo-db2 su - db2inst1 -c "db2stop"

# DB2インスタンスの再起動
docker exec -it air-cargo-db2 su - db2inst1 -c "db2stop force"
docker exec -it air-cargo-db2 su - db2inst1 -c "db2start"

# データベース接続テスト
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 connect to AIRCARGO"

# データベースの作成（存在しない場合）
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 create database AIRCARGO"

# データベースのアクティベート
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 activate database AIRCARGO"
```

#### 8. 正しいDB2コマンド実行方法
```bash
# 基本的なDB2コマンド実行
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 'SELECT * FROM SYSCAT.TABLES'"

# パフォーマンス監視クエリ
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 'SELECT * FROM SYSIBMADM.SNAPAPPL_INFO'"

# スロークエリの特定
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 'SELECT * FROM SYSIBMADM.SNAPSTMT WHERE TOTAL_EXEC_TIME > 1000'"

# ロックの確認
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 'SELECT * FROM SYSIBMADM.SNAPLOCK'"

# テーブル一覧の確認
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 'SELECT TABNAME FROM SYSCAT.TABLES WHERE TABSCHEMA = \'DB2INST1\''"

# データベース設定の確認
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 get database manager configuration"
```

#### 9. データベース初期化の問題
```bash
# 初期化スクリプトの実行
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 connect to AIRCARGO"
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 -f /var/custom/01_create_database.sql"

# 手動でのテーブル作成
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 connect to AIRCARGO"
docker exec -it air-cargo-db2 su - db2inst1 -c "db2 'CREATE TABLE CARGO (CARGO_ID VARCHAR(20) NOT NULL PRIMARY KEY)'"
```

#### 10. 権限の問題
```bash
# データベースディレクトリの権限確認
docker exec -it air-cargo-db2 ls -la /database/

# 権限の修正
docker exec -it air-cargo-db2 chown -R db2inst1:db2iadm1 /database/
docker exec -it air-cargo-db2 chmod -R 755 /database/

# DB2インスタンスユーザーの確認
docker exec -it air-cargo-db2 id db2inst1
```

### ログの確認
```bash
# 全サービスのログ
docker-compose logs

# 個別サービスのログ
docker-compose logs -f db2
docker-compose logs -f backend
docker-compose logs -f frontend
```

## 運用管理

### サービスの管理
```bash
# サービスの起動
docker-compose up -d

# サービスの停止
docker-compose down

# サービスの再起動
docker-compose restart

# サービスの状態確認
docker-compose ps
```

### データのバックアップ
```bash
# DB2データベースのバックアップ
docker exec air-cargo-db2 db2 backup database AIRCARGO to /database/backup
```

### パフォーマンス監視
```bash
# 定期的なパフォーマンスチェック
docker exec air-cargo-db2 /scripts/performance_monitor.sh

# メンテナンススクリプトの実行
docker exec air-cargo-db2 /scripts/maintenance.sh
```

## 詳細なドキュメント
- [API ドキュメント](docs/API_DOCUMENTATION.md)
- [デプロイメントガイド](docs/DEPLOYMENT_GUIDE.md)
- [DB2 チューニングガイド](docs/DB2_TUNING_GUIDE.md)

## DB2 テーブルキー項目修正手順

### 1. 主キー（Primary Key）の修正

#### 1.1 主キーの追加
```sql
-- 既存テーブルに主キーを追加
ALTER TABLE CARGO ADD CONSTRAINT PK_CARGO PRIMARY KEY (CARGO_ID);

-- 複合主キーの追加
ALTER TABLE TRACKING ADD CONSTRAINT PK_TRACKING PRIMARY KEY (CARGO_ID, TIMESTAMP);
```

#### 1.2 主キーの変更
```sql
-- 1. 既存の主キー制約を削除
ALTER TABLE CARGO DROP PRIMARY KEY;

-- 2. 新しい主キー制約を追加
ALTER TABLE CARGO ADD CONSTRAINT PK_CARGO_NEW PRIMARY KEY (CARGO_ID, FLIGHT_NUMBER);
```

#### 1.3 主キーの削除
```sql
-- 主キー制約の削除
ALTER TABLE CARGO DROP PRIMARY KEY;
```

### 2. 外部キー（Foreign Key）の修正

#### 2.1 外部キーの追加
```sql
-- 外部キー制約の追加
ALTER TABLE INBOUND 
ADD CONSTRAINT FK_INBOUND_CARGO 
FOREIGN KEY (CARGO_ID) REFERENCES CARGO(CARGO_ID);

-- 複合外部キーの追加
ALTER TABLE TRACKING 
ADD CONSTRAINT FK_TRACKING_CARGO 
FOREIGN KEY (CARGO_ID, FLIGHT_NUMBER) 
REFERENCES CARGO(CARGO_ID, FLIGHT_NUMBER);
```

#### 2.2 外部キーの変更
```sql
-- 1. 既存の外部キー制約を削除
ALTER TABLE INBOUND DROP CONSTRAINT FK_INBOUND_CARGO;

-- 2. 新しい外部キー制約を追加
ALTER TABLE INBOUND 
ADD CONSTRAINT FK_INBOUND_CARGO_NEW 
FOREIGN KEY (CARGO_ID) REFERENCES CARGO(CARGO_ID) 
ON DELETE CASCADE;
```

#### 2.3 外部キーの削除
```sql
-- 外部キー制約の削除
ALTER TABLE INBOUND DROP CONSTRAINT FK_INBOUND_CARGO;
```

### 3. インデックスの修正

#### 3.1 インデックスの追加
```sql
-- 単一カラムインデックス
CREATE INDEX IDX_CARGO_STATUS ON CARGO(STATUS);

-- 複合インデックス
CREATE INDEX IDX_CARGO_FLIGHT_STATUS ON CARGO(FLIGHT_NUMBER, STATUS);

-- ユニークインデックス
CREATE UNIQUE INDEX IDX_CARGO_UNIQUE ON CARGO(CARGO_ID, FLIGHT_NUMBER);

-- 部分インデックス（特定条件のみ）
CREATE INDEX IDX_CARGO_ACTIVE ON CARGO(STATUS) WHERE STATUS IN ('PENDING', 'IN_TRANSIT');
```

#### 3.2 インデックスの変更
```sql
-- 1. 既存インデックスを削除
DROP INDEX IDX_CARGO_STATUS;

-- 2. 新しいインデックスを作成
CREATE INDEX IDX_CARGO_STATUS_NEW ON CARGO(STATUS, CREATED_DATE);
```

#### 3.3 インデックスの削除
```sql
-- インデックスの削除
DROP INDEX IDX_CARGO_STATUS;
```

### 4. 制約（Constraint）の修正

#### 4.1 制約の追加
```sql
-- NOT NULL制約の追加
ALTER TABLE CARGO ALTER COLUMN CARGO_ID SET NOT NULL;

-- CHECK制約の追加
ALTER TABLE CARGO 
ADD CONSTRAINT CHK_CARGO_STATUS 
CHECK (STATUS IN ('PENDING', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED'));

-- DEFAULT制約の追加
ALTER TABLE CARGO ALTER COLUMN CREATED_DATE SET DEFAULT CURRENT TIMESTAMP;
```

#### 4.2 制約の変更
```sql
-- 1. 既存制約を削除
ALTER TABLE CARGO DROP CONSTRAINT CHK_CARGO_STATUS;

-- 2. 新しい制約を追加
ALTER TABLE CARGO 
ADD CONSTRAINT CHK_CARGO_STATUS_NEW 
CHECK (STATUS IN ('PENDING', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED', 'ON_HOLD'));
```

#### 4.3 制約の削除
```sql
-- 制約の削除
ALTER TABLE CARGO DROP CONSTRAINT CHK_CARGO_STATUS;
```

### 5. 安全なキー修正手順

#### 5.1 事前準備
```sql
-- 1. 現在の制約状況を確認
SELECT * FROM SYSCAT.TABCONST WHERE TABNAME = 'CARGO';
SELECT * FROM SYSCAT.INDEXES WHERE TABNAME = 'CARGO';
SELECT * FROM SYSCAT.KEYCOLUSE WHERE TABNAME = 'CARGO';

-- 2. 外部キー参照を確認
SELECT * FROM SYSCAT.REFERENCES WHERE TABNAME = 'CARGO';
SELECT * FROM SYSCAT.REFERENCES WHERE REFTABNAME = 'CARGO';

-- 3. データの整合性を確認
SELECT COUNT(*) FROM CARGO WHERE CARGO_ID IS NULL;
SELECT COUNT(*) FROM INBOUND WHERE CARGO_ID NOT IN (SELECT CARGO_ID FROM CARGO);
```

#### 5.2 段階的修正手順
```sql
-- ステップ1: バックアップの作成
-- テーブル構造のバックアップ
db2look -d AIRCARGO -t CARGO -o cargo_backup.sql

-- データのバックアップ
db2 "EXPORT TO cargo_data.del OF DEL SELECT * FROM CARGO"

-- ステップ2: 一時テーブルの作成
CREATE TABLE CARGO_TEMP LIKE CARGO;

-- ステップ3: データの移行
INSERT INTO CARGO_TEMP SELECT * FROM CARGO;

-- ステップ4: 制約の削除
ALTER TABLE INBOUND DROP CONSTRAINT FK_INBOUND_CARGO;
ALTER TABLE OUTBOUND DROP CONSTRAINT FK_OUTBOUND_CARGO;
ALTER TABLE TRACKING DROP CONSTRAINT FK_TRACKING_CARGO;
ALTER TABLE CARGO DROP PRIMARY KEY;

-- ステップ5: 新しい制約の追加
ALTER TABLE CARGO ADD CONSTRAINT PK_CARGO_NEW PRIMARY KEY (CARGO_ID, FLIGHT_NUMBER);

-- ステップ6: 外部キーの再作成
ALTER TABLE INBOUND 
ADD CONSTRAINT FK_INBOUND_CARGO_NEW 
FOREIGN KEY (CARGO_ID, FLIGHT_NUMBER) 
REFERENCES CARGO(CARGO_ID, FLIGHT_NUMBER);

-- ステップ7: 一時テーブルの削除
DROP TABLE CARGO_TEMP;
```

### 6. エラー処理とロールバック

#### 6.1 エラー時のロールバック手順
```sql
-- エラーが発生した場合のロールバック
BEGIN ATOMIC
    -- 制約の削除
    ALTER TABLE INBOUND DROP CONSTRAINT FK_INBOUND_CARGO_NEW;
    ALTER TABLE CARGO DROP CONSTRAINT PK_CARGO_NEW;
    
    -- 元の制約を復元
    ALTER TABLE CARGO ADD CONSTRAINT PK_CARGO PRIMARY KEY (CARGO_ID);
    ALTER TABLE INBOUND 
    ADD CONSTRAINT FK_INBOUND_CARGO 
    FOREIGN KEY (CARGO_ID) REFERENCES CARGO(CARGO_ID);
END;
```

#### 6.2 データ整合性チェック
```sql
-- 修正後の整合性チェック
-- 主キーの重複チェック
SELECT CARGO_ID, COUNT(*) 
FROM CARGO 
GROUP BY CARGO_ID 
HAVING COUNT(*) > 1;

-- 外部キー参照整合性チェック
SELECT i.CARGO_ID 
FROM INBOUND i 
LEFT JOIN CARGO c ON i.CARGO_ID = c.CARGO_ID 
WHERE c.CARGO_ID IS NULL;

-- 制約違反チェック
SELECT * FROM CARGO WHERE STATUS NOT IN ('PENDING', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED');
```

### 7. パフォーマンス最適化

#### 7.1 修正後の最適化
```sql
-- 統計情報の更新
RUNSTATS ON TABLE CARGO WITH DISTRIBUTION AND DETAILED INDEXES ALL;

-- テーブルの最適化
REORG TABLE CARGO ALLOW READ ACCESS;

-- インデックスの最適化
REORG INDEXES ALL FOR TABLE CARGO ALLOW READ ACCESS;
```

#### 7.2 パフォーマンス監視
```sql
-- 修正後のパフォーマンス確認
-- インデックス使用状況
SELECT * FROM SYSCAT.INDEXES WHERE TABNAME = 'CARGO';

-- クエリパフォーマンス
SELECT * FROM SYSIBMADM.SNAPSTMT WHERE TABNAME = 'CARGO' ORDER BY TOTAL_EXEC_TIME DESC;

-- ロック状況
SELECT * FROM SYSIBMADM.SNAPLOCK WHERE TABNAME = 'CARGO';
```

### 8. 自動化スクリプト

#### 8.1 キー修正スクリプト
```bash
#!/bin/bash
# DB2テーブルキー修正スクリプト

# 設定
DB_NAME="AIRCARGO"
DB_USER="db2inst1"
DB_PASSWORD="db2inst1"
TABLE_NAME="CARGO"

echo "=== DB2テーブルキー修正スクリプト ==="
echo "対象テーブル: $TABLE_NAME"
echo "データベース: $DB_NAME"

# DB2に接続
db2 connect to $DB_NAME user $DB_USER using $DB_PASSWORD

if [ $? -ne 0 ]; then
    echo "データベース接続に失敗しました"
    exit 1
fi

echo "データベースに接続しました"

# バックアップの作成
echo "バックアップを作成中..."
db2 "EXPORT TO ${TABLE_NAME}_backup.del OF DEL SELECT * FROM $TABLE_NAME"
db2look -d $DB_NAME -t $TABLE_NAME -o ${TABLE_NAME}_backup.sql

# 現在の制約状況を確認
echo "現在の制約状況を確認中..."
db2 "SELECT * FROM SYSCAT.TABCONST WHERE TABNAME = '$TABLE_NAME'"

# 修正の実行
echo "キー修正を実行中..."
db2 "ALTER TABLE $TABLE_NAME DROP PRIMARY KEY"
db2 "ALTER TABLE $TABLE_NAME ADD CONSTRAINT PK_${TABLE_NAME}_NEW PRIMARY KEY (CARGO_ID, FLIGHT_NUMBER)"

# 外部キーの更新
echo "外部キーを更新中..."
db2 "ALTER TABLE INBOUND DROP CONSTRAINT FK_INBOUND_CARGO"
db2 "ALTER TABLE INBOUND ADD CONSTRAINT FK_INBOUND_CARGO_NEW FOREIGN KEY (CARGO_ID, FLIGHT_NUMBER) REFERENCES $TABLE_NAME(CARGO_ID, FLIGHT_NUMBER)"

# 整合性チェック
echo "データ整合性をチェック中..."
db2 "SELECT COUNT(*) FROM $TABLE_NAME WHERE CARGO_ID IS NULL"

# 最適化
echo "パフォーマンス最適化を実行中..."
db2 "RUNSTATS ON TABLE $TABLE_NAME WITH DISTRIBUTION AND DETAILED INDEXES ALL"
db2 "REORG TABLE $TABLE_NAME ALLOW READ ACCESS"

echo "キー修正が完了しました"

# 接続を切断
db2 disconnect all
```

#### 8.2 ロールバックスクリプト
```bash
#!/bin/bash
# DB2テーブルキー修正ロールバックスクリプト

# 設定
DB_NAME="AIRCARGO"
DB_USER="db2inst1"
DB_PASSWORD="db2inst1"
TABLE_NAME="CARGO"

echo "=== DB2テーブルキー修正ロールバックスクリプト ==="

# DB2に接続
db2 connect to $DB_NAME user $DB_USER using $DB_PASSWORD

if [ $? -ne 0 ]; then
    echo "データベース接続に失敗しました"
    exit 1
fi

echo "ロールバックを実行中..."

# 新しい制約を削除
db2 "ALTER TABLE INBOUND DROP CONSTRAINT FK_INBOUND_CARGO_NEW"
db2 "ALTER TABLE $TABLE_NAME DROP CONSTRAINT PK_${TABLE_NAME}_NEW"

# 元の制約を復元
db2 "ALTER TABLE $TABLE_NAME ADD CONSTRAINT PK_${TABLE_NAME} PRIMARY KEY (CARGO_ID)"
db2 "ALTER TABLE INBOUND ADD CONSTRAINT FK_INBOUND_CARGO FOREIGN KEY (CARGO_ID) REFERENCES $TABLE_NAME(CARGO_ID)"

# バックアップからデータを復元（必要に応じて）
if [ -f "${TABLE_NAME}_backup.del" ]; then
    echo "バックアップからデータを復元中..."
    db2 "DELETE FROM $TABLE_NAME"
    db2 "IMPORT FROM ${TABLE_NAME}_backup.del OF DEL INSERT INTO $TABLE_NAME"
fi

echo "ロールバックが完了しました"

# 接続を切断
db2 disconnect all
```

### 9. ベストプラクティス

#### 9.1 修正前のチェックリスト
- [ ] データベースのバックアップを作成
- [ ] 現在の制約状況を文書化
- [ ] 外部キー参照を確認
- [ ] データ整合性をチェック
- [ ] メンテナンス時間を確保
- [ ] ロールバック手順を準備

#### 9.2 修正時の注意事項
- 本番環境での修正は慎重に行う
- 段階的に修正を実行する
- 各ステップで整合性をチェックする
- エラー時のロールバック手順を準備する
- パフォーマンスへの影響を監視する

#### 9.3 修正後の確認事項
- [ ] データ整合性の確認
- [ ] アプリケーションの動作確認
- [ ] パフォーマンスの確認
- [ ] インデックスの最適化
- [ ] 統計情報の更新
- [ ] ドキュメントの更新

## DB2 テーブル構造修正手順

### 1. カラム名の変更

#### 1.1 単一カラム名の変更
```sql
-- カラム名の変更
ALTER TABLE CARGO RENAME COLUMN SHIPPER_NAME TO SENDER_NAME;

-- 複数カラム名の変更
ALTER TABLE CARGO RENAME COLUMN CONSIGNEE_NAME TO RECIPIENT_NAME;
ALTER TABLE CARGO RENAME COLUMN ORIGIN_AIRPORT TO DEPARTURE_AIRPORT;
ALTER TABLE CARGO RENAME COLUMN DESTINATION_AIRPORT TO ARRIVAL_AIRPORT;
```

#### 1.2 変更後の整合性確認
```sql
-- カラム名変更後の確認
SELECT COLNAME, TYPENAME, LENGTH, NULLS 
FROM SYSCAT.COLUMNS 
WHERE TABNAME = 'CARGO' AND TABSCHEMA = 'DB2INST1';

-- インデックスの確認
SELECT * FROM SYSCAT.INDEXES WHERE TABNAME = 'CARGO';

-- 制約の確認
SELECT * FROM SYSCAT.TABCONST WHERE TABNAME = 'CARGO';
```

### 2. データ型の変更

#### 2.1 互換性のあるデータ型変更
```sql
-- VARCHARの長さ変更（拡張のみ）
ALTER TABLE CARGO ALTER COLUMN FLIGHT_NUMBER SET DATA TYPE VARCHAR(15);

-- NUMERICの精度変更
ALTER TABLE CARGO ALTER COLUMN WEIGHT SET DATA TYPE DECIMAL(10,2);

-- DATE型の変更
ALTER TABLE CARGO ALTER COLUMN CREATED_DATE SET DATA TYPE TIMESTAMP;
```

#### 2.2 互換性のないデータ型変更
```sql
-- 1. 新しいカラムを追加
ALTER TABLE CARGO ADD COLUMN STATUS_NEW INTEGER;

-- 2. データを変換して移行
UPDATE CARGO SET STATUS_NEW = 
    CASE STATUS 
        WHEN 'PENDING' THEN 1
        WHEN 'IN_TRANSIT' THEN 2
        WHEN 'DELIVERED' THEN 3
        WHEN 'CANCELLED' THEN 4
        ELSE 0
    END;

-- 3. 古いカラムを削除
ALTER TABLE CARGO DROP COLUMN STATUS;

-- 4. 新しいカラム名を変更
ALTER TABLE CARGO RENAME COLUMN STATUS_NEW TO STATUS;
```

### 3. カラム属性の変更

#### 3.1 NULL/NOT NULL制約の変更
```sql
-- NOT NULL制約の追加
ALTER TABLE CARGO ALTER COLUMN CARGO_ID SET NOT NULL;

-- NOT NULL制約の削除
ALTER TABLE CARGO ALTER COLUMN DESCRIPTION DROP NOT NULL;
```

#### 3.2 DEFAULT値の変更
```sql
-- DEFAULT値の設定
ALTER TABLE CARGO ALTER COLUMN CREATED_DATE SET DEFAULT CURRENT TIMESTAMP;

-- DEFAULT値の削除
ALTER TABLE CARGO ALTER COLUMN STATUS DROP DEFAULT;
```

#### 3.3 制約の変更
```sql
-- CHECK制約の追加
ALTER TABLE CARGO 
ADD CONSTRAINT CHK_WEIGHT_POSITIVE 
CHECK (WEIGHT > 0);

-- CHECK制約の削除
ALTER TABLE CARGO DROP CONSTRAINT CHK_WEIGHT_POSITIVE;
```

### 4. カラムの追加・削除

#### 4.1 カラムの追加
```sql
-- 単一カラムの追加
ALTER TABLE CARGO ADD COLUMN PRIORITY_LEVEL INTEGER DEFAULT 1;

-- 複数カラムの追加
ALTER TABLE CARGO 
ADD COLUMN HANDLER_ID VARCHAR(20),
ADD COLUMN HANDLER_NAME VARCHAR(100),
ADD COLUMN HANDLER_PHONE VARCHAR(20);

-- NOT NULL制約付きカラムの追加
ALTER TABLE CARGO ADD COLUMN URGENCY_LEVEL INTEGER NOT NULL DEFAULT 1;
```

#### 4.2 カラムの削除
```sql
-- 依存関係の確認
SELECT * FROM SYSCAT.KEYCOLUSE WHERE TABNAME = 'CARGO';
SELECT * FROM SYSCAT.INDEXES WHERE TABNAME = 'CARGO';

-- カラムの削除
ALTER TABLE CARGO DROP COLUMN DESCRIPTION;

-- 複数カラムの削除
ALTER TABLE CARGO 
DROP COLUMN HANDLER_PHONE,
DROP COLUMN URGENCY_LEVEL;
```

### 5. テーブル構造の大規模変更

#### 5.1 テーブル再作成による構造変更
```sql
-- ステップ1: 新しいテーブル構造の作成
CREATE TABLE CARGO_NEW (
    CARGO_ID VARCHAR(20) NOT NULL,
    FLIGHT_NUMBER VARCHAR(15) NOT NULL,
    SENDER_NAME VARCHAR(100) NOT NULL,
    RECIPIENT_NAME VARCHAR(100) NOT NULL,
    DEPARTURE_AIRPORT VARCHAR(10) NOT NULL,
    ARRIVAL_AIRPORT VARCHAR(10) NOT NULL,
    WEIGHT DECIMAL(10,2) NOT NULL,
    STATUS INTEGER NOT NULL DEFAULT 1,
    PRIORITY_LEVEL INTEGER DEFAULT 1,
    HANDLER_ID VARCHAR(20),
    CREATED_DATE TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    UPDATED_DATE TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    VERSION INTEGER DEFAULT 1,
    PRIMARY KEY (CARGO_ID, FLIGHT_NUMBER)
);

-- ステップ2: データの移行
INSERT INTO CARGO_NEW (
    CARGO_ID, FLIGHT_NUMBER, SENDER_NAME, RECIPIENT_NAME,
    DEPARTURE_AIRPORT, ARRIVAL_AIRPORT, WEIGHT, STATUS,
    PRIORITY_LEVEL, HANDLER_ID, CREATED_DATE, UPDATED_DATE, VERSION
)
SELECT 
    CARGO_ID, FLIGHT_NUMBER, SHIPPER_NAME, CONSIGNEE_NAME,
    ORIGIN_AIRPORT, DESTINATION_AIRPORT, WEIGHT,
    CASE STATUS 
        WHEN 'PENDING' THEN 1
        WHEN 'IN_TRANSIT' THEN 2
        WHEN 'DELIVERED' THEN 3
        WHEN 'CANCELLED' THEN 4
        ELSE 1
    END,
    COALESCE(PRIORITY_LEVEL, 1),
    HANDLER_ID,
    CREATED_DATE,
    UPDATED_DATE,
    VERSION
FROM CARGO;

-- ステップ3: 古いテーブルの削除
DROP TABLE CARGO;

-- ステップ4: 新しいテーブル名の変更
RENAME TABLE CARGO_NEW TO CARGO;
```

### 6. インデックスの再構築

#### 6.1 インデックスの再作成
```sql
-- 既存インデックスの削除
DROP INDEX IDX_CARGO_FLIGHT_STATUS;
DROP INDEX IDX_CARGO_SHIPPER;

-- 新しいインデックスの作成
CREATE INDEX IDX_CARGO_FLIGHT_STATUS ON CARGO(FLIGHT_NUMBER, STATUS);
CREATE INDEX IDX_CARGO_SENDER ON CARGO(SENDER_NAME);
CREATE INDEX IDX_CARGO_RECIPIENT ON CARGO(RECIPIENT_NAME);
CREATE INDEX IDX_CARGO_AIRPORTS ON CARGO(DEPARTURE_AIRPORT, ARRIVAL_AIRPORT);
```

#### 6.2 外部キーの再構築
```sql
-- 外部キー制約の削除
ALTER TABLE INBOUND DROP CONSTRAINT FK_INBOUND_CARGO;
ALTER TABLE OUTBOUND DROP CONSTRAINT FK_OUTBOUND_CARGO;
ALTER TABLE TRACKING DROP CONSTRAINT FK_TRACKING_CARGO;

-- 新しい外部キー制約の追加
ALTER TABLE INBOUND 
ADD CONSTRAINT FK_INBOUND_CARGO 
FOREIGN KEY (CARGO_ID, FLIGHT_NUMBER) 
REFERENCES CARGO(CARGO_ID, FLIGHT_NUMBER);

ALTER TABLE OUTBOUND 
ADD CONSTRAINT FK_OUTBOUND_CARGO 
FOREIGN KEY (CARGO_ID, FLIGHT_NUMBER) 
REFERENCES CARGO(CARGO_ID, FLIGHT_NUMBER);

ALTER TABLE TRACKING 
ADD CONSTRAINT FK_TRACKING_CARGO 
FOREIGN KEY (CARGO_ID, FLIGHT_NUMBER) 
REFERENCES CARGO(CARGO_ID, FLIGHT_NUMBER);
```

### 7. ビューとストアドプロシージャの更新

#### 7.1 ビューの更新
```sql
-- 既存ビューの削除
DROP VIEW V_CARGO_SUMMARY;

-- 新しいビューの作成
CREATE VIEW V_CARGO_SUMMARY AS
SELECT 
    CARGO_ID,
    FLIGHT_NUMBER,
    SENDER_NAME,
    RECIPIENT_NAME,
    DEPARTURE_AIRPORT,
    ARRIVAL_AIRPORT,
    WEIGHT,
    CASE STATUS 
        WHEN 1 THEN 'PENDING'
        WHEN 2 THEN 'IN_TRANSIT'
        WHEN 3 THEN 'DELIVERED'
        WHEN 4 THEN 'CANCELLED'
        ELSE 'UNKNOWN'
    END AS STATUS_TEXT,
    PRIORITY_LEVEL,
    CREATED_DATE
FROM CARGO;
```

#### 7.2 ストアドプロシージャの更新
```sql
-- 既存プロシージャの削除
DROP PROCEDURE SP_GET_CARGO_BY_STATUS;

-- 新しいプロシージャの作成
CREATE PROCEDURE SP_GET_CARGO_BY_STATUS (
    IN P_STATUS INTEGER,
    OUT P_COUNT INTEGER
)
LANGUAGE SQL
BEGIN
    SELECT COUNT(*) INTO P_COUNT 
    FROM CARGO 
    WHERE STATUS = P_STATUS;
    
    SELECT * FROM CARGO WHERE STATUS = P_STATUS;
END;
```

### 8. アプリケーションコードの更新

#### 8.1 Doma2エンティティの更新
```java
@Entity
@Table(name = "CARGO")
public class Cargo {
    
    @Id
    @Column(name = "CARGO_ID")
    private String cargoId;
    
    @Id
    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;
    
    @Column(name = "SENDER_NAME")
    private String senderName;
    
    @Column(name = "RECIPIENT_NAME")
    private String recipientName;
    
    @Column(name = "DEPARTURE_AIRPORT")
    private String departureAirport;
    
    @Column(name = "ARRIVAL_AIRPORT")
    private String arrivalAirport;
    
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    
    @Column(name = "STATUS")
    private Integer status;
    
    @Column(name = "PRIORITY_LEVEL")
    private Integer priorityLevel;
    
    @Column(name = "HANDLER_ID")
    private String handlerId;
    
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
    
    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;
    
    @Version
    @Column(name = "VERSION")
    private Integer version;
    
    // ゲッター・セッター
    // ステータス定数
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_IN_TRANSIT = 2;
    public static final int STATUS_DELIVERED = 3;
    public static final int STATUS_CANCELLED = 4;
}
```

#### 8.2 リポジトリの更新
```java
@Dao
@ConfigAutowireable
public interface CargoRepository {
    
    @Select
    List<Cargo> findByStatus(Integer status);
    
    @Select
    List<Cargo> findBySenderNameContainingIgnoreCase(String senderName);
    
    @Select
    List<Cargo> findByRecipientNameContainingIgnoreCase(String recipientName);
    
    @Select
    List<Cargo> findByDepartureAirportAndArrivalAirport(String departureAirport, String arrivalAirport);
    
    @Select
    List<Cargo> findByPriorityLevelGreaterThan(Integer priorityLevel);
}
```

### 9. データ移行スクリプト

#### 9.1 自動データ移行スクリプト
```bash
#!/bin/bash
# DB2テーブル構造変更スクリプト

# 設定
DB_NAME="AIRCARGO"
DB_USER="db2inst1"
DB_PASSWORD="db2inst1"
TABLE_NAME="CARGO"

echo "=== DB2テーブル構造変更スクリプト ==="
echo "対象テーブル: $TABLE_NAME"

# DB2に接続
db2 connect to $DB_NAME user $DB_USER using $DB_PASSWORD

if [ $? -ne 0 ]; then
    echo "データベース接続に失敗しました"
    exit 1
fi

echo "データベースに接続しました"

# バックアップの作成
echo "バックアップを作成中..."
db2 "EXPORT TO ${TABLE_NAME}_backup.del OF DEL SELECT * FROM $TABLE_NAME"
db2look -d $DB_NAME -t $TABLE_NAME -o ${TABLE_NAME}_backup.sql

# 新しいテーブル構造の作成
echo "新しいテーブル構造を作成中..."
db2 "CREATE TABLE ${TABLE_NAME}_NEW (
    CARGO_ID VARCHAR(20) NOT NULL,
    FLIGHT_NUMBER VARCHAR(15) NOT NULL,
    SENDER_NAME VARCHAR(100) NOT NULL,
    RECIPIENT_NAME VARCHAR(100) NOT NULL,
    DEPARTURE_AIRPORT VARCHAR(10) NOT NULL,
    ARRIVAL_AIRPORT VARCHAR(10) NOT NULL,
    WEIGHT DECIMAL(10,2) NOT NULL,
    STATUS INTEGER NOT NULL DEFAULT 1,
    PRIORITY_LEVEL INTEGER DEFAULT 1,
    HANDLER_ID VARCHAR(20),
    CREATED_DATE TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    UPDATED_DATE TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    VERSION INTEGER DEFAULT 1,
    PRIMARY KEY (CARGO_ID, FLIGHT_NUMBER)
)"

# データの移行
echo "データを移行中..."
db2 "INSERT INTO ${TABLE_NAME}_NEW (
    CARGO_ID, FLIGHT_NUMBER, SENDER_NAME, RECIPIENT_NAME,
    DEPARTURE_AIRPORT, ARRIVAL_AIRPORT, WEIGHT, STATUS,
    PRIORITY_LEVEL, HANDLER_ID, CREATED_DATE, UPDATED_DATE, VERSION
)
SELECT 
    CARGO_ID, FLIGHT_NUMBER, SHIPPER_NAME, CONSIGNEE_NAME,
    ORIGIN_AIRPORT, DESTINATION_AIRPORT, WEIGHT,
    CASE STATUS 
        WHEN 'PENDING' THEN 1
        WHEN 'IN_TRANSIT' THEN 2
        WHEN 'DELIVERED' THEN 3
        WHEN 'CANCELLED' THEN 4
        ELSE 1
    END,
    COALESCE(PRIORITY_LEVEL, 1),
    HANDLER_ID,
    CREATED_DATE,
    UPDATED_DATE,
    VERSION
FROM $TABLE_NAME"

# 古いテーブルの削除
echo "古いテーブルを削除中..."
db2 "DROP TABLE $TABLE_NAME"

# 新しいテーブル名の変更
echo "テーブル名を変更中..."
db2 "RENAME TABLE ${TABLE_NAME}_NEW TO $TABLE_NAME"

# インデックスの再作成
echo "インデックスを再作成中..."
db2 "CREATE INDEX IDX_CARGO_FLIGHT_STATUS ON $TABLE_NAME(FLIGHT_NUMBER, STATUS)"
db2 "CREATE INDEX IDX_CARGO_SENDER ON $TABLE_NAME(SENDER_NAME)"
db2 "CREATE INDEX IDX_CARGO_RECIPIENT ON $TABLE_NAME(RECIPIENT_NAME)"
db2 "CREATE INDEX IDX_CARGO_AIRPORTS ON $TABLE_NAME(DEPARTURE_AIRPORT, ARRIVAL_AIRPORT)"

# 統計情報の更新
echo "統計情報を更新中..."
db2 "RUNSTATS ON TABLE $TABLE_NAME WITH DISTRIBUTION AND DETAILED INDEXES ALL"

echo "テーブル構造変更が完了しました"

# 接続を切断
db2 disconnect all
```

### 10. 検証とテスト

#### 10.1 データ整合性の検証
```sql
-- データ件数の確認
SELECT COUNT(*) FROM CARGO;
SELECT COUNT(*) FROM CARGO_BACKUP;

-- データ内容の確認
SELECT * FROM CARGO LIMIT 10;

-- ステータス値の確認
SELECT STATUS, COUNT(*) 
FROM CARGO 
GROUP BY STATUS;

-- 必須項目のNULLチェック
SELECT COUNT(*) FROM CARGO WHERE CARGO_ID IS NULL;
SELECT COUNT(*) FROM CARGO WHERE FLIGHT_NUMBER IS NULL;
SELECT COUNT(*) FROM CARGO WHERE SENDER_NAME IS NULL;
```

#### 10.2 パフォーマンステスト
```sql
-- インデックス使用状況の確認
SELECT * FROM SYSCAT.INDEXES WHERE TABNAME = 'CARGO';

-- クエリパフォーマンスの確認
SELECT * FROM SYSIBMADM.SNAPSTMT WHERE TABNAME = 'CARGO' ORDER BY TOTAL_EXEC_TIME DESC;

-- テーブルサイズの確認
SELECT TABSCHEMA, TABNAME, CARD, NPAGES, FPAGES 
FROM SYSCAT.TABLES 
WHERE TABNAME = 'CARGO';
```

### 11. ロールバック手順

#### 11.1 緊急ロールバック
```bash
#!/bin/bash
# 緊急ロールバックスクリプト

# 設定
DB_NAME="AIRCARGO"
DB_USER="db2inst1"
DB_PASSWORD="db2inst1"
TABLE_NAME="CARGO"

echo "=== 緊急ロールバックスクリプト ==="

# DB2に接続
db2 connect to $DB_NAME user $DB_USER using $DB_PASSWORD

if [ $? -ne 0 ]; then
    echo "データベース接続に失敗しました"
    exit 1
fi

echo "ロールバックを実行中..."

# 現在のテーブルを削除
db2 "DROP TABLE $TABLE_NAME"

# バックアップから復元
if [ -f "${TABLE_NAME}_backup.del" ]; then
    echo "バックアップから復元中..."
    db2 "IMPORT FROM ${TABLE_NAME}_backup.del OF DEL INSERT INTO $TABLE_NAME"
fi

# 元のインデックスを復元
db2 "CREATE INDEX IDX_CARGO_FLIGHT_STATUS ON $TABLE_NAME(FLIGHT_NUMBER, STATUS)"
db2 "CREATE INDEX IDX_CARGO_SHIPPER ON $TABLE_NAME(SHIPPER_NAME)"

echo "ロールバックが完了しました"

# 接続を切断
db2 disconnect all
```

### 12. ベストプラクティス

#### 12.1 変更前の準備
- [ ] 完全なバックアップの作成
- [ ] テスト環境での検証
- [ ] アプリケーションコードの準備
- [ ] メンテナンス時間の確保
- [ ] ロールバック手順の準備

#### 12.2 変更時の注意事項
- 本番環境での変更は慎重に行う
- 段階的に変更を実行する
- 各ステップでデータ整合性をチェックする
- パフォーマンスへの影響を監視する
- エラー時のロールバック手順を準備する

#### 12.3 変更後の確認事項
- [ ] データ整合性の確認
- [ ] アプリケーションの動作確認
- [ ] パフォーマンスの確認
- [ ] インデックスの最適化
- [ ] 統計情報の更新
- [ ] ドキュメントの更新
- [ ] 運用チームへの通知 
