package jp.co.iko.layoutconverter.audit;

import java.time.LocalDateTime;

/**
 * {@code EXECUTION_CONTROL} の 1 レコードを表す値オブジェクト。
 *
 * @param runId 実行 ID
 * @param taskId Task ID
 * @param status 実行ステータス
 * @param startAt 開始日時
 * @param endAt 終了日時。未終了の場合は {@code null}
 */
public record ExecutionControlRecord(
        String runId,
        String taskId,
        ExecutionStatus status,
        LocalDateTime startAt,
        LocalDateTime endAt
) {
}
