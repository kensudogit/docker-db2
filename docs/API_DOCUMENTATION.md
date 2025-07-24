# 航空貨物ロジステックシステム API ドキュメント

## 概要
航空貨物ロジステックシステムのREST API仕様書です。

## ベースURL
```
http://localhost:8080/api
```

## 認証
現在はBasic認証を使用しています。
- ユーザー名: admin
- パスワード: admin123

## エンドポイント

### 貨物管理

#### 1. 全貨物取得
```
GET /cargo
```

**レスポンス例:**
```json
[
  {
    "cargoId": "CARGO-ABC12345",
    "flightNumber": "NH001",
    "originAirport": "NRT",
    "destinationAirport": "LAX",
    "cargoType": "一般貨物",
    "weight": 100.5,
    "volume": 2.5,
    "status": "PENDING",
    "shipperName": "田中太郎",
    "consigneeName": "John Smith",
    "createdDate": "2024-01-01T10:00:00",
    "updatedDate": "2024-01-01T10:00:00"
  }
]
```

#### 2. 貨物詳細取得
```
GET /cargo/{cargoId}
```

#### 3. 貨物作成
```
POST /cargo
```

**リクエストボディ:**
```json
{
  "flightNumber": "NH001",
  "originAirport": "NRT",
  "destinationAirport": "LAX",
  "cargoType": "一般貨物",
  "weight": 100.5,
  "volume": 2.5,
  "shipperName": "田中太郎",
  "consigneeName": "John Smith"
}
```

#### 4. 貨物更新
```
PUT /cargo/{cargoId}
```

#### 5. 貨物削除
```
DELETE /cargo/{cargoId}
```

#### 6. ステータス別貨物取得
```
GET /cargo/status/{status}
```

#### 7. フライト別貨物取得
```
GET /cargo/flight/{flightNumber}
```

#### 8. 貨物検索
```
GET /cargo/search?keyword={keyword}
```

#### 9. 貨物追跡情報取得
```
GET /cargo/{cargoId}/tracking
```

#### 10. 追跡情報作成
```
POST /cargo/{cargoId}/tracking
```

**リクエストボディ:**
```json
{
  "location": "成田空港",
  "status": "到着済み",
  "handlerId": "H001",
  "notes": "貨物が正常に到着しました"
}
```

#### 11. ステータス別統計
```
GET /cargo/stats/status/{status}
```

### 入荷管理

#### 1. 全入荷取得
```
GET /inbound
```

#### 2. 入荷詳細取得
```
GET /inbound/{inboundId}
```

#### 3. 入荷作成
```
POST /inbound
```

**リクエストボディ:**
```json
{
  "cargoId": "CARGO-ABC12345",
  "flightNumber": "NH001",
  "arrivalDate": "2024-01-01",
  "arrivalTime": "10:00:00",
  "terminal": "T1",
  "handlerId": "H001",
  "notes": "貨物が正常に到着しました"
}
```

#### 4. 入荷ステータス更新
```
PUT /inbound/{inboundId}/status?status={status}
```

### 出荷管理

#### 1. 全出荷取得
```
GET /outbound
```

#### 2. 出荷詳細取得
```
GET /outbound/{outboundId}
```

#### 3. 出荷作成
```
POST /outbound
```

**リクエストボディ:**
```json
{
  "cargoId": "CARGO-ABC12345",
  "flightNumber": "NH002",
  "departureDate": "2024-01-02",
  "departureTime": "14:00:00",
  "terminal": "T1",
  "handlerId": "H002",
  "notes": "貨物の積込が完了しました"
}
```

#### 4. 出荷ステータス更新
```
PUT /outbound/{outboundId}/status?status={status}
```

## ステータス定義

### 貨物ステータス
- `PENDING`: 待機中
- `IN_TRANSIT`: 輸送中
- `ARRIVED`: 到着済み
- `DELIVERED`: 配達済み
- `CANCELLED`: キャンセル

### 入荷ステータス
- `ARRIVED`: 到着済み
- `UNLOADING`: 荷下ろし中
- `INSPECTION`: 検査中
- `READY`: 準備完了
- `DELIVERED`: 配達済み

### 出荷ステータス
- `SCHEDULED`: 予定済み
- `READY`: 準備完了
- `LOADING`: 積込中
- `DEPARTED`: 出発済み
- `IN_TRANSIT`: 輸送中

## エラーレスポンス

### 400 Bad Request
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "バリデーションエラー",
  "path": "/api/cargo"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "貨物が見つかりません",
  "path": "/api/cargo/CARGO-ABC12345"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "サーバー内部エラー",
  "path": "/api/cargo"
}
```

## Swagger UI
APIドキュメントの詳細は以下のURLで確認できます：
```
http://localhost:8080/api/swagger-ui.html
``` 