package jp.co.iko.layoutconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * iko-layout-converter の Spring Boot 起動クラス。
 *
 * <p>JP1 などのジョブ実行基盤から CLI アプリケーションとして起動される想定。</p>
 */
@SpringBootApplication
public class IkoLayoutConverterApplication {

    /**
     * Spring Boot が利用する既定コンストラクタ。
     */
    public IkoLayoutConverterApplication() {
    }

    /**
     * アプリケーションを起動する。
     *
     * @param args Spring Boot と MigrationRunner に渡す起動引数
     */
    public static void main(String[] args) {
        SpringApplication.run(IkoLayoutConverterApplication.class, args);
    }
}
