package jp.co.iko.layoutconverter.task;

/**
 * Task 実行時に共有するコンテキスト。
 *
 * @param runId 実行 ID
 * @param currentSchema 現行スキーマ名
 * @param nextSchema 次期スキーマ名
 * @param commitSize コミット件数
 */
public record MigrationTaskContext(
        String runId,
        String currentSchema,
        String nextSchema,
        int commitSize
) {
}
