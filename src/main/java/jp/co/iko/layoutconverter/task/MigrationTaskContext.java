package jp.co.iko.layoutconverter.task;

/**
 * Task 実行時に共有するコンテキスト。
 *
 * @param runId 実行 ID
 * @param sourceSchema 移行元スキーマ名
 * @param targetSchema 移行先スキーマ名
 * @param commitSize コミット件数
 */
public record MigrationTaskContext(
        String runId,
        String sourceSchema,
        String targetSchema,
        int commitSize
) {
}
