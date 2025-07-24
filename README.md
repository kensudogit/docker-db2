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

## 詳細なドキュメント
- [API ドキュメント](docs/API_DOCUMENTATION.md)
- [デプロイメントガイド](docs/DEPLOYMENT_GUIDE.md) 
