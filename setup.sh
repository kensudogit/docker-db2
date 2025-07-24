#!/bin/bash

echo "航空貨物ロジステックシステム セットアップスクリプト"
echo "================================================"

# Docker と Docker Compose の確認
if ! command -v docker &> /dev/null; then
    echo "エラー: Docker がインストールされていません"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "エラー: Docker Compose がインストールされていません"
    exit 1
fi

echo "✓ Docker と Docker Compose が利用可能です"

# 既存のコンテナとボリュームをクリーンアップ
echo "既存のコンテナとボリュームをクリーンアップしています..."
docker-compose down -v
docker system prune -f

# アプリケーションをビルドして起動
echo "アプリケーションをビルドして起動しています..."
docker-compose up --build -d

# 起動確認
echo "サービス起動を確認しています..."
sleep 30

# ヘルスチェック
echo "ヘルスチェックを実行しています..."

# DB2 の確認
if docker-compose ps db2 | grep -q "Up"; then
    echo "✓ DB2 データベースが起動しています"
else
    echo "✗ DB2 データベースの起動に失敗しました"
fi

# WebSphere の確認
if docker-compose ps websphere | grep -q "Up"; then
    echo "✓ WebSphere アプリケーションサーバーが起動しています"
else
    echo "✗ WebSphere アプリケーションサーバーの起動に失敗しました"
fi

# バックエンド の確認
if docker-compose ps backend | grep -q "Up"; then
    echo "✓ Spring Boot バックエンドが起動しています"
else
    echo "✗ Spring Boot バックエンドの起動に失敗しました"
fi

# フロントエンド の確認
if docker-compose ps frontend | grep -q "Up"; then
    echo "✓ Next.js フロントエンドが起動しています"
else
    echo "✗ Next.js フロントエンドの起動に失敗しました"
fi

echo ""
echo "セットアップ完了！"
echo "=================="
echo "アクセスURL:"
echo "- フロントエンド: http://localhost:3000"
echo "- バックエンド API: http://localhost:8080/api"
echo "- Swagger UI: http://localhost:8080/api/swagger-ui.html"
echo "- WebSphere 管理コンソール: http://localhost:9043/ibm/console"
echo "- DB2 管理コンソール: http://localhost:50000"
echo ""
echo "ログの確認:"
echo "docker-compose logs -f [service_name]"
echo ""
echo "サービスの停止:"
echo "docker-compose down" 