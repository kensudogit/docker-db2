# 航空貨物ロジステックシステム デプロイメントガイド

## 前提条件

### 必要なソフトウェア
- Docker (version 20.10以上)
- Docker Compose (version 2.0以上)
- 最低8GB RAM
- 最低20GB 空き容量

### システム要件
- Linux, macOS, Windows 10/11
- ネットワーク接続（Dockerイメージのダウンロード用）

## インストール手順

### 1. リポジトリのクローン
```bash
git clone <repository-url>
cd docker-db2
```

### 2. セットアップスクリプトの実行
```bash
chmod +x setup.sh
./setup.sh
```

### 3. 手動セットアップ（オプション）
セットアップスクリプトを使用しない場合：

```bash
# 既存のコンテナとボリュームをクリーンアップ
docker-compose down -v
docker system prune -f

# アプリケーションをビルドして起動
docker-compose up --build -d

# 起動確認
docker-compose ps
```

## サービス構成

### 1. DB2 データベース
- **ポート**: 50000
- **データベース名**: AIRCARGO
- **ユーザー**: db2inst1
- **パスワード**: db2inst1
- **永続化**: Docker volume (db2_data)

### 2. WebSphere Application Server
- **管理コンソール**: http://localhost:9043/ibm/console
- **アプリケーション**: http://localhost:9080
- **管理者**: wsadmin / wsadmin123

### 3. Spring Boot バックエンド
- **API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **認証**: admin / admin123

### 4. Next.js フロントエンド
- **アプリケーション**: http://localhost:3000

## 環境変数

### Docker Compose 環境変数
```yaml
# DB2
DB2INST1_PASSWORD: db2inst1
DBNAME: AIRCARGO
LICENSE: accept

# WebSphere
PROFILE_NAME: AppSrv01
CELL_NAME: DefaultCell01
NODE_NAME: DefaultNode01
SERVER_NAME: server1
ADMIN_USER_NAME: wsadmin
ADMIN_PASSWORD: wsadmin123

# Spring Boot
SPRING_PROFILES_ACTIVE: docker
DB2_HOST: db2
DB2_PORT: 50000
DB2_DATABASE: AIRCARGO
DB2_USER: db2inst1
DB2_PASSWORD: db2inst1

# Next.js
NEXT_PUBLIC_API_URL: http://localhost:8080/api
```

## 運用管理

### サービスの起動
```bash
# 全サービス起動
docker-compose up -d

# 個別サービス起動
docker-compose up -d db2
docker-compose up -d websphere
docker-compose up -d backend
docker-compose up -d frontend
```

### サービスの停止
```bash
# 全サービス停止
docker-compose down

# 個別サービス停止
docker-compose stop db2
docker-compose stop websphere
docker-compose stop backend
docker-compose stop frontend
```

### ログの確認
```bash
# 全サービスのログ
docker-compose logs

# 個別サービスのログ
docker-compose logs -f db2
docker-compose logs -f websphere
docker-compose logs -f backend
docker-compose logs -f frontend
```

### データベースのバックアップ
```bash
# DB2 データベースのバックアップ
docker exec air-cargo-db2 db2 backup database AIRCARGO to /database/backup
```

### データベースの復元
```bash
# DB2 データベースの復元
docker exec air-cargo-db2 db2 restore database AIRCARGO from /database/backup
```

## トラブルシューティング

### よくある問題

#### 1. ポート競合
```bash
# 使用中のポートを確認
netstat -tulpn | grep :8080
netstat -tulpn | grep :3000
netstat -tulpn | grep :50000

# 競合するプロセスを停止
sudo kill -9 <PID>
```

#### 2. メモリ不足
```bash
# Docker のメモリ制限を確認
docker stats

# 不要なコンテナを削除
docker container prune
docker image prune
```

#### 3. データベース接続エラー
```bash
# DB2 の状態確認
docker-compose logs db2

# データベースの初期化確認
docker exec air-cargo-db2 db2 list database directory
```

#### 4. アプリケーション起動エラー
```bash
# Spring Boot アプリケーションのログ確認
docker-compose logs backend

# データベース接続テスト
docker exec air-cargo-backend curl http://localhost:8080/api/actuator/health
```

### ログレベルの変更
```yaml
# application.yml
logging:
  level:
    com.aircargo: DEBUG
    org.springframework.security: DEBUG
```

## パフォーマンスチューニング

### 1. JVM オプション
```yaml
# docker-compose.yml
environment:
  - JAVA_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC
```

### 2. データベース設定
```sql
-- DB2 パフォーマンス設定
UPDATE DBM CFG USING SVCENAME 50000 IMMEDIATE;
UPDATE DBM CFG USING MAX_CONNECTIONS 1000 IMMEDIATE;
```

### 3. WebSphere 設定
- JVM ヒープサイズ: 最小512MB、最大1024MB
- コネクションプール: 最大50接続
- スレッドプール: 最大100スレッド

## セキュリティ

### 1. パスワード変更
```bash
# DB2 パスワード変更
docker exec air-cargo-db2 db2 connect to AIRCARGO user db2inst1
docker exec air-cargo-db2 db2 grant dbadm on database to user db2inst1

# WebSphere パスワード変更
# WebSphere 管理コンソールから変更

# Spring Boot パスワード変更
# application.yml で変更
```

### 2. ファイアウォール設定
```bash
# 必要なポートのみ開放
sudo ufw allow 3000/tcp  # フロントエンド
sudo ufw allow 8080/tcp  # バックエンド
sudo ufw allow 50000/tcp # DB2
```

### 3. SSL/TLS 設定
```yaml
# application.yml
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: password
    key-store-type: PKCS12
```

## 監視

### 1. ヘルスチェック
```bash
# アプリケーションのヘルスチェック
curl http://localhost:8080/api/actuator/health

# データベースのヘルスチェック
docker exec air-cargo-db2 db2 connect to AIRCARGO
```

### 2. メトリクス
```bash
# Spring Boot メトリクス
curl http://localhost:8080/api/actuator/metrics

# システムメトリクス
docker stats
```

### 3. アラート設定
- メモリ使用率: 80%以上
- CPU使用率: 90%以上
- ディスク使用率: 85%以上
- データベース接続数: 80%以上

## アップグレード

### 1. アプリケーションのアップグレード
```bash
# 新しいイメージをビルド
docker-compose build

# サービスを再起動
docker-compose up -d
```

### 2. データベースのマイグレーション
```bash
# マイグレーションスクリプトの実行
docker exec air-cargo-db2 db2 -f /var/custom/migration.sql
```

## サポート

### ログファイルの場所
- アプリケーションログ: `./backend/logs/`
- データベースログ: Docker volume (db2_data)
- WebSphereログ: Docker volume (websphere_data)

### 連絡先
- 技術サポート: support@aircargo.com
- ドキュメント: https://docs.aircargo.com 