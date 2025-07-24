FROM openjdk:11-jdk-slim

WORKDIR /app

# Maven Wrapper と pom.xml をコピー
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Maven Wrapper に実行権限を付与
RUN chmod +x mvnw

# 依存関係をダウンロード
RUN ./mvnw dependency:go-offline -B

# ソースコードをコピー
COPY src src

# アプリケーションをビルド
RUN ./mvnw clean package -DskipTests

# 実行可能JARファイルを抽出
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# 実行環境用のイメージを作成
FROM openjdk:11-jre-slim

VOLUME /tmp

# 依存関係とアプリケーションをコピー
COPY --from=0 /app/target/dependency/BOOT-INF/lib /app/lib
COPY --from=0 /app/target/dependency/META-INF /app/META-INF
COPY --from=0 /app/target/dependency/BOOT-INF/classes /app

# ログディレクトリを作成
RUN mkdir -p /app/logs

# アプリケーションを実行
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.aircargo.AirCargoApplication"] 