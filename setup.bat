@echo off
echo 航空貨物ロジステックシステム セットアップスクリプト
echo ================================================

REM Docker と Docker Compose の確認
docker --version >nul 2>&1
if errorlevel 1 (
    echo エラー: Docker がインストールされていません
    pause
    exit /b 1
)

docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo エラー: Docker Compose がインストールされていません
    pause
    exit /b 1
)

echo ✓ Docker と Docker Compose が利用可能です

REM 既存のコンテナとボリュームをクリーンアップ
echo 既存のコンテナとボリュームをクリーンアップしています...
docker-compose down -v
docker system prune -f

REM アプリケーションをビルドして起動
echo アプリケーションをビルドして起動しています...
echo 注意: 初回起動時はGradleの依存関係ダウンロードとDB2の初期化に時間がかかります（5-10分）
docker-compose up --build -d

REM 起動確認
echo サービス起動を確認しています...
timeout /t 30 /nobreak >nul

REM ヘルスチェック
echo ヘルスチェックを実行しています...

REM DB2 の確認
docker-compose ps db2 | findstr "Up" >nul
if errorlevel 1 (
    echo ✗ DB2 データベースの起動に失敗しました
) else (
    echo ✓ DB2 データベースが起動しています
)

REM バックエンド の確認
docker-compose ps backend | findstr "Up" >nul
if errorlevel 1 (
    echo ✗ Spring Boot バックエンドの起動に失敗しました
) else (
    echo ✓ Spring Boot バックエンドが起動しています
)

REM フロントエンド の確認
docker-compose ps frontend | findstr "Up" >nul
if errorlevel 1 (
    echo ✗ Next.js フロントエンドの起動に失敗しました
) else (
    echo ✓ Next.js フロントエンドが起動しています
)

echo.
echo セットアップ完了！
echo ==================
echo アクセスURL:
echo - フロントエンド: http://localhost:3000
echo - バックエンド API: http://localhost:8080/api
echo - Swagger UI: http://localhost:8080/api/swagger-ui.html
echo - DB2 管理コンソール: http://localhost:50000
echo.
echo 開発用コマンド:
echo - バックエンド (Gradle): cd backend ^&^& ./gradlew bootRun
echo - フロントエンド: cd frontend ^&^& npm run dev
echo.
echo ログの確認:
echo docker-compose logs -f [service_name]
echo.
echo サービスの停止:
echo docker-compose down
echo.
pause 