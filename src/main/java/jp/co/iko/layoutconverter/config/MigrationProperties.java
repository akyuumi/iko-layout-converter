package jp.co.iko.layoutconverter.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@code migration.*} 配下の設定値。
 *
 * <p>現行・次期スキーマ名、コミット件数、実行対象 Task ID を保持する。</p>
 */
@ConfigurationProperties(prefix = "migration")
public class MigrationProperties {

    private String currentSchema;
    private String nextSchema;
    private int commitSize = 10000;
    private List<String> tasks = new ArrayList<>();

    /**
     * Spring Boot の設定バインドで利用する既定コンストラクタ。
     */
    public MigrationProperties() {
    }

    /**
     * 現行スキーマ名を取得する。
     *
     * @return 現行スキーマ名
     */
    public String getCurrentSchema() {
        return currentSchema;
    }

    /**
     * 現行スキーマ名を設定する。
     *
     * @param currentSchema 現行スキーマ名
     */
    public void setCurrentSchema(String currentSchema) {
        this.currentSchema = currentSchema;
    }

    /**
     * 次期スキーマ名を取得する。
     *
     * @return 次期スキーマ名
     */
    public String getNextSchema() {
        return nextSchema;
    }

    /**
     * 次期スキーマ名を設定する。
     *
     * @param nextSchema 次期スキーマ名
     */
    public void setNextSchema(String nextSchema) {
        this.nextSchema = nextSchema;
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
