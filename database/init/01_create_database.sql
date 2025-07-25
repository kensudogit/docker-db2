-- =====================================================
-- 航空貨物ロジステックシステム データベース初期化スクリプト
-- =====================================================
-- 
-- このスクリプトは航空貨物管理システムのデータベース構造を定義します。
-- 貨物、入荷、出荷、追跡、作業員、空港、フライトの各テーブルを作成し、
-- 必要なインデックスとサンプルデータを挿入します。
-- =====================================================

-- =====================================================
-- 貨物テーブル（メインテーブル）
-- =====================================================
-- 航空貨物の基本情報を管理するテーブル
-- 貨物ID、フライト番号、発着空港、貨物タイプ、重量、容積、ステータス、
-- 荷送人・荷受人情報、作成・更新日時を保持
CREATE TABLE CARGO (
    CARGO_ID VARCHAR(20) NOT NULL PRIMARY KEY,        -- 貨物ID（主キー）
    FLIGHT_NUMBER VARCHAR(10) NOT NULL,               -- フライト番号
    ORIGIN_AIRPORT VARCHAR(3) NOT NULL,               -- 出発空港コード
    DESTINATION_AIRPORT VARCHAR(3) NOT NULL,          -- 到着空港コード
    CARGO_TYPE VARCHAR(50) NOT NULL,                  -- 貨物タイプ（一般貨物、危険物、生鮮品など）
    WEIGHT DECIMAL(10,2) NOT NULL,                    -- 重量（kg）
    VOLUME DECIMAL(10,2) NOT NULL,                    -- 容積（m³）
    STATUS VARCHAR(20) NOT NULL DEFAULT 'PENDING',    -- ステータス（PENDING、IN_TRANSIT、DELIVERED等）
    SHIPPER_NAME VARCHAR(100) NOT NULL,               -- 荷送人名
    CONSIGNEE_NAME VARCHAR(100) NOT NULL,             -- 荷受人名
    CREATED_DATE TIMESTAMP,                           -- 作成日時
    UPDATED_DATE TIMESTAMP                            -- 更新日時
);

-- =====================================================
-- 入荷テーブル
-- =====================================================
-- 貨物が空港に到着した際の入荷記録を管理するテーブル
-- 貨物ID、フライト番号、到着日時、ターミナル、ステータス、担当者情報を保持
CREATE TABLE INBOUND (
    INBOUND_ID VARCHAR(20) NOT NULL PRIMARY KEY,      -- 入荷ID（主キー）
    CARGO_ID VARCHAR(20) NOT NULL,                    -- 貨物ID（外部キー）
    FLIGHT_NUMBER VARCHAR(10) NOT NULL,               -- フライト番号
    ARRIVAL_DATE DATE NOT NULL,                       -- 到着日
    ARRIVAL_TIME TIME NOT NULL,                       -- 到着時刻
    TERMINAL VARCHAR(10) NOT NULL,                    -- ターミナル番号
    STATUS VARCHAR(20) NOT NULL DEFAULT 'ARRIVED',    -- ステータス（ARRIVED、PROCESSING、COMPLETED等）
    HANDLER_ID VARCHAR(20),                           -- 担当者ID
    NOTES VARCHAR(1000),                              -- 備考
    CREATED_DATE TIMESTAMP,                           -- 作成日時
    FOREIGN KEY (CARGO_ID) REFERENCES CARGO(CARGO_ID) -- 貨物テーブルへの外部キー制約
);

-- =====================================================
-- 出荷テーブル
-- =====================================================
-- 貨物が空港から出発する際の出荷記録を管理するテーブル
-- 貨物ID、フライト番号、出発日時、ターミナル、ステータス、担当者情報を保持
CREATE TABLE OUTBOUND (
    OUTBOUND_ID VARCHAR(20) NOT NULL PRIMARY KEY,     -- 出荷ID（主キー）
    CARGO_ID VARCHAR(20) NOT NULL,                    -- 貨物ID（外部キー）
    FLIGHT_NUMBER VARCHAR(10) NOT NULL,               -- フライト番号
    DEPARTURE_DATE DATE NOT NULL,                     -- 出発日
    DEPARTURE_TIME TIME NOT NULL,                     -- 出発時刻
    TERMINAL VARCHAR(10) NOT NULL,                    -- ターミナル番号
    STATUS VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',  -- ステータス（SCHEDULED、READY、DEPARTED等）
    HANDLER_ID VARCHAR(20),                           -- 担当者ID
    NOTES VARCHAR(1000),                              -- 備考
    CREATED_DATE TIMESTAMP,                           -- 作成日時
    FOREIGN KEY (CARGO_ID) REFERENCES CARGO(CARGO_ID) -- 貨物テーブルへの外部キー制約
);

-- =====================================================
-- 追跡テーブル
-- =====================================================
-- 貨物の移動状況やステータス変更の履歴を管理するテーブル
-- 貨物ID、位置情報、ステータス、タイムスタンプ、担当者情報を保持
CREATE TABLE TRACKING (
    TRACKING_ID VARCHAR(20) NOT NULL PRIMARY KEY,     -- 追跡ID（主キー）
    CARGO_ID VARCHAR(20) NOT NULL,                    -- 貨物ID（外部キー）
    LOCATION VARCHAR(100) NOT NULL,                   -- 位置情報（空港名、ターミナル名など）
    STATUS VARCHAR(50) NOT NULL,                      -- ステータス（IN_TRANSIT、ARRIVED、DEPARTED等）
    TIMESTAMP TIMESTAMP,                              -- タイムスタンプ
    HANDLER_ID VARCHAR(20),                           -- 担当者ID
    NOTES VARCHAR(1000),                              -- 備考
    FOREIGN KEY (CARGO_ID) REFERENCES CARGO(CARGO_ID) -- 貨物テーブルへの外部キー制約
);

-- =====================================================
-- ハンドラー（作業員）テーブル
-- =====================================================
-- 貨物処理を担当する作業員の情報を管理するテーブル
-- 作業員ID、氏名、部署、連絡先情報を保持
CREATE TABLE HANDLER (
    HANDLER_ID VARCHAR(20) NOT NULL PRIMARY KEY,      -- 作業員ID（主キー）
    HANDLER_NAME VARCHAR(100) NOT NULL,               -- 作業員氏名
    DEPARTMENT VARCHAR(50) NOT NULL,                  -- 部署名
    CONTACT_NUMBER VARCHAR(20),                       -- 連絡先電話番号
    EMAIL VARCHAR(100),                               -- メールアドレス
    STATUS VARCHAR(20) DEFAULT 'ACTIVE',              -- ステータス（ACTIVE、INACTIVE等）
    CREATED_DATE TIMESTAMP                            -- 作成日時
);

-- =====================================================
-- 空港テーブル
-- =====================================================
-- 空港の基本情報を管理するテーブル
-- 空港コード、空港名、都市、国、タイムゾーンを保持
CREATE TABLE AIRPORT (
    AIRPORT_CODE VARCHAR(3) NOT NULL PRIMARY KEY,     -- 空港コード（主キー）
    AIRPORT_NAME VARCHAR(100) NOT NULL,               -- 空港名
    CITY VARCHAR(50) NOT NULL,                        -- 都市名
    COUNTRY VARCHAR(50) NOT NULL,                     -- 国名
    TIMEZONE VARCHAR(10) NOT NULL                     -- タイムゾーン
);

-- =====================================================
-- フライトテーブル
-- =====================================================
-- フライトの基本情報を管理するテーブル
-- フライト番号、発着空港、発着時刻、機種、ステータスを保持
CREATE TABLE FLIGHT (
    FLIGHT_NUMBER VARCHAR(10) NOT NULL PRIMARY KEY,   -- フライト番号（主キー）
    ORIGIN_AIRPORT VARCHAR(3) NOT NULL,               -- 出発空港コード
    DESTINATION_AIRPORT VARCHAR(3) NOT NULL,          -- 到着空港コード
    DEPARTURE_TIME TIME NOT NULL,                     -- 出発時刻
    ARRIVAL_TIME TIME NOT NULL,                       -- 到着時刻
    AIRCRAFT_TYPE VARCHAR(20),                        -- 機種
    STATUS VARCHAR(20) DEFAULT 'SCHEDULED',           -- ステータス（SCHEDULED、DELAYED、CANCELLED等）
    FOREIGN KEY (ORIGIN_AIRPORT) REFERENCES AIRPORT(AIRPORT_CODE),      -- 出発空港への外部キー制約
    FOREIGN KEY (DESTINATION_AIRPORT) REFERENCES AIRPORT(AIRPORT_CODE)  -- 到着空港への外部キー制約
);

-- =====================================================
-- インデックス作成
-- =====================================================
-- 検索パフォーマンス向上のためのインデックスを作成

-- 貨物テーブルのインデックス
CREATE INDEX IDX_CARGO_FLIGHT ON CARGO(FLIGHT_NUMBER);    -- フライト番号による検索用
CREATE INDEX IDX_CARGO_STATUS ON CARGO(STATUS);           -- ステータスによる検索用

-- 入荷テーブルのインデックス
CREATE INDEX IDX_INBOUND_DATE ON INBOUND(ARRIVAL_DATE);   -- 到着日による検索用

-- 出荷テーブルのインデックス
CREATE INDEX IDX_OUTBOUND_DATE ON OUTBOUND(DEPARTURE_DATE); -- 出発日による検索用

-- 追跡テーブルのインデックス
CREATE INDEX IDX_TRACKING_CARGO ON TRACKING(CARGO_ID);    -- 貨物IDによる検索用
CREATE INDEX IDX_TRACKING_TIMESTAMP ON TRACKING(TIMESTAMP); -- タイムスタンプによる検索用

-- =====================================================
-- サンプルデータ挿入
-- =====================================================

-- 空港データの挿入
INSERT INTO AIRPORT (AIRPORT_CODE, AIRPORT_NAME, CITY, COUNTRY, TIMEZONE) VALUES
('NRT', 'Narita International Airport', 'Tokyo', 'Japan', 'Asia/Tokyo'),
('HND', 'Haneda Airport', 'Tokyo', 'Japan', 'Asia/Tokyo'),
('LAX', 'Los Angeles International Airport', 'Los Angeles', 'USA', 'America/Los_Angeles'),
('JFK', 'John F. Kennedy International Airport', 'New York', 'USA', 'America/New_York'),
('LHR', 'London Heathrow Airport', 'London', 'UK', 'Europe/London'),
('CDG', 'Charles de Gaulle Airport', 'Paris', 'France', 'Europe/Paris');

-- 作業員データの挿入
INSERT INTO HANDLER (HANDLER_ID, HANDLER_NAME, DEPARTMENT, CONTACT_NUMBER, EMAIL) VALUES
('H001', '田中太郎', '入荷部門', '090-1234-5678', 'tanaka@aircargo.com'),
('H002', '佐藤花子', '出荷部門', '090-2345-6789', 'sato@aircargo.com'),
('H003', '鈴木一郎', '追跡部門', '090-3456-7890', 'suzuki@aircargo.com');

-- フライトデータの挿入
INSERT INTO FLIGHT (FLIGHT_NUMBER, ORIGIN_AIRPORT, DESTINATION_AIRPORT, DEPARTURE_TIME, ARRIVAL_TIME, AIRCRAFT_TYPE) VALUES
('NH001', 'NRT', 'LAX', '10:00:00', '06:00:00', 'Boeing 787'),
('NH002', 'LAX', 'NRT', '14:00:00', '18:00:00', 'Boeing 787'),
('JL001', 'HND', 'JFK', '11:00:00', '14:00:00', 'Boeing 777'),
('JL002', 'JFK', 'HND', '16:00:00', '20:00:00', 'Boeing 777'); 