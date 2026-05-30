package jp.co.iko.layoutconverter.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 移行ツールの Spring 設定。
 *
 * <p>{@link MigrationProperties} を設定プロパティとして有効化する。</p>
 */
@Configuration
@EnableConfigurationProperties(MigrationProperties.class)
public class MigrationConfig {

    /**
     * Spring が利用する既定コンストラクタ。
     */
    public MigrationConfig() {
    }
}
