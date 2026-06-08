package jp.co.iko.layoutconverter.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@code migration.*} 配下の設定値。
 *
 * <p>移行元・移行先スキーマ名、コミット件数、実行対象 Task ID を保持する。</p>
 */
@ConfigurationProperties(prefix = "migration")
public class MigrationProperties {

    private String sourceSchema;
    private String targetSchema;
    private int commitSize = 10000;
    private List<String> tasks = new ArrayList<>();

    /**
     * Spring Boot の設定バインドで利用する既定コンストラクタ。
     */
    public MigrationProperties() {
    }

    /**
     * 移行元スキーマ名を取得する。
     *
     * @return 移行元スキーマ名
     */
    public String getSourceSchema() {
        return sourceSchema;
    }

    /**
     * 移行元スキーマ名を設定する。
     *
     * @param sourceSchema 移行元スキーマ名
     */
    public void setSourceSchema(String sourceSchema) {
        this.sourceSchema = sourceSchema;
    }

    /**
     * 移行先スキーマ名を取得する。
     *
     * @return 移行先スキーマ名
     */
    public String getTargetSchema() {
        return targetSchema;
    }

    /**
     * 移行先スキーマ名を設定する。
     *
     * @param targetSchema 移行先スキーマ名
     */
    public void setTargetSchema(String targetSchema) {
        this.targetSchema = targetSchema;
    }

    /**
     * コミット件数を取得する。
     *
     * @return コミット件数
     */
    public int getCommitSize() {
        return commitSize;
    }

    /**
     * コミット件数を設定する。
     *
     * @param commitSize コミット件数
     */
    public void setCommitSize(int commitSize) {
        this.commitSize = commitSize;
    }

    /**
     * 実行対象 Task ID の一覧を取得する。
     *
     * @return 実行対象 Task ID の一覧
     */
    public List<String> getTasks() {
        return tasks;
    }

    /**
     * 実行対象 Task ID の一覧を設定する。
     *
     * @param tasks 実行対象 Task ID の一覧
     */
    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }
}
