# 航空貨物管理システム テストガイド

このドキュメントは、航空貨物管理システムのテストクラスについて説明します。

## テスト構造

```
src/test/java/com/aircargo/
├── AirCargoApplicationTests.java          # メインアプリケーションのテスト
├── entity/
│   ├── CargoTest.java                    # 貨物エンティティのテスト
│   ├── InboundTest.java                  # 入荷エンティティのテスト
│   ├── OutboundTest.java                 # 出荷エンティティのテスト
│   └── TrackingTest.java                 # 追跡エンティティのテスト
├── repository/
│   └── CargoRepositoryTest.java          # 貨物リポジトリのテスト
├── service/
│   └── CargoServiceTest.java             # 貨物サービスのテスト
└── controller/
    └── CargoControllerTest.java          # 貨物コントローラーのテスト
```

## テストの種類

### 1. エンティティテスト（Entity Tests）
- **目的**: エンティティクラスのプロパティ、コンストラクタ、Lombokアノテーションの動作をテスト
- **対象クラス**: `Cargo`, `Inbound`, `Outbound`, `Tracking`
- **テスト内容**:
  - プロパティの設定と取得
  - 全引数コンストラクタ
  - 無引数コンストラクタ
  - equals/hashCodeメソッド
  - toStringメソッド

### 2. リポジトリテスト（Repository Tests）
- **目的**: データアクセス層のメソッドをテスト
- **対象クラス**: `CargoRepository`
- **テスト内容**:
  - CRUD操作（作成、読み取り、更新、削除）
  - カスタムクエリメソッド
  - 検索機能
- **使用技術**: H2インメモリデータベース、Spring Data JPA

### 3. サービステスト（Service Tests）
- **目的**: ビジネスロジックをテスト
- **対象クラス**: `CargoService`
- **テスト内容**:
  - 貨物管理のビジネスロジック
  - 入荷・出荷・追跡関連の処理
  - 例外処理
- **使用技術**: Mockito（依存関係のモック化）

### 4. コントローラーテスト（Controller Tests）
- **目的**: REST APIエンドポイントをテスト
- **対象クラス**: `CargoController`
- **テスト内容**:
  - HTTPリクエストとレスポンス
  - ステータスコード
  - JSONレスポンスの内容
- **使用技術**: MockMvc、Mockito

### 5. アプリケーションテスト（Application Tests）
- **目的**: Spring Bootアプリケーションの起動をテスト
- **対象クラス**: `AirCargoApplication`
- **テスト内容**:
  - アプリケーションコンテキストの読み込み
  - Beanの設定確認

## テストの実行方法

### 全テストの実行
```bash
./gradlew test
```

### 特定のテストクラスの実行
```bash
./gradlew test --tests CargoTest
./gradlew test --tests CargoServiceTest
./gradlew test --tests CargoControllerTest
```

### テストレポートの確認
```bash
./gradlew test
# レポートは build/reports/tests/test/index.html に生成されます
```

## テスト設定

### テスト用データベース
- **データベース**: H2インメモリデータベース
- **設定ファイル**: `src/test/resources/application-test.yml`
- **プロファイル**: `test`

### テストデータ
各テストクラスで独自のテストデータを作成し、テスト後にクリーンアップします。

## テストカバレッジ

### エンティティテスト
- プロパティの設定・取得: 100%
- コンストラクタ: 100%
- Lombokアノテーション: 100%

### リポジトリテスト
- 基本的なCRUD操作: 100%
- カスタムクエリメソッド: 100%
- 検索機能: 100%

### サービステスト
- ビジネスロジック: 90%以上
- 例外処理: 100%
- 依存関係のモック化: 100%

### コントローラーテスト
- RESTエンドポイント: 100%
- HTTPステータスコード: 100%
- JSONレスポンス: 100%

## テストの追加方法

### 新しいエンティティのテスト
1. `src/test/java/com/aircargo/entity/` にテストクラスを作成
2. プロパティ、コンストラクタ、Lombokアノテーションのテストを実装

### 新しいサービスのテスト
1. `src/test/java/com/aircargo/service/` にテストクラスを作成
2. Mockitoを使用して依存関係をモック化
3. ビジネスロジックと例外処理をテスト

### 新しいコントローラーのテスト
1. `src/test/java/com/aircargo/controller/` にテストクラスを作成
2. MockMvcを使用してHTTPリクエストをテスト
3. レスポンスの内容とステータスコードを検証

## 注意事項

1. **テストデータの独立性**: 各テストは独立して実行できるように設計されています
2. **データベースのクリーンアップ**: テスト後にデータベースは自動的にクリーンアップされます
3. **モックの使用**: 外部依存関係は適切にモック化されています
4. **日本語コメント**: すべてのテストクラスに日本語コメントを追加しています

## トラブルシューティング

### テストが失敗する場合
1. データベース接続の確認
2. 依存関係の確認
3. テストデータの整合性確認

### テストの実行が遅い場合
1. 並列実行の設定確認
2. 不要なテストの削除
3. モックの適切な使用 