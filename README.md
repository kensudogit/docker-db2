# 航空貨物ロジステックシステム

## 概要
航空貨物の入出荷管理と追跡機能を提供するシステムです。

## 技術スタック
- **データベース**: IBM DB2
- **アプリケーションサーバ**: IBM WebSphere
- **バックエンド**: Java (Spring Boot)
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
├── backend/                 # Java Spring Boot アプリケーション
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
docker-compose up -d websphere
docker-compose up -d backend
docker-compose up -d frontend
```

### 3. アクセス
- フロントエンド: http://localhost:3000
- バックエンド API: http://localhost:8080
- WebSphere 管理コンソール: http://localhost:9043/ibm/console
- DB2 管理コンソール: http://localhost:50000

## 開発ガイド
詳細な開発ガイドは `docs/` ディレクトリを参照してください。 "# docker-db2" 
