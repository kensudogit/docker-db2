FROM gradle:7.6.1-jdk11 AS build

WORKDIR /app

# Gradle設定ファイルをコピー
COPY build.gradle settings.gradle gradle.properties ./

# 依存関係をダウンロード
RUN gradle dependencies --no-daemon

# ソースコードをコピー
COPY src src

# アプリケーションをビルド
RUN gradle build --no-daemon

# 実行環境用のイメージを作成
FROM openjdk:11-jre-slim

WORKDIR /app

# アプリケーションJARファイルをコピー
COPY --from=build /app/build/libs/air-cargo-logistics.jar app.jar

# ログディレクトリを作成
RUN mkdir -p /app/logs

# アプリケーションを実行
ENTRYPOINT ["java", "-jar", "app.jar"] 