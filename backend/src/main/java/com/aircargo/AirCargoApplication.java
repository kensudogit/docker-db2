package com.aircargo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 航空貨物管理システムのメインアプリケーションクラス
 * 
 * このクラスはSpring Bootアプリケーションのエントリーポイントです。
 * JPA監査機能を有効にして、エンティティの作成日時や更新日時の自動管理を行います。
 */
@SpringBootApplication
@EnableJpaAuditing
public class AirCargoApplication {

    /**
     * アプリケーションのメインメソッド
     * Spring Bootアプリケーションを起動します
     * 
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        SpringApplication.run(AirCargoApplication.class, args);
    }
} 