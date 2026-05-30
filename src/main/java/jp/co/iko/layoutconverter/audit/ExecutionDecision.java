package jp.co.iko.layoutconverter.audit;

/**
 * 実行制御による Task 実行可否の判定結果。
 *
 * @param executable 今回実行する場合は {@code true}
 * @param previousStatus 直近実行のステータス。初回実行の場合は {@code null}
 * @param reason 判定理由
 */
public record ExecutionDecision(
        boolean executable,
        ExecutionStatus previousStatus,
        String reason
) {

    /**
     * 実行可能な判定結果を作成する。
     *
     * @param previousStatus 直近実行のステータス
     * @param reason 判定理由
     * @return 実行可能な判定結果
     */
    public static ExecutionDecision execute(ExecutionStatus previousStatus, String reason) {
        return new ExecutionDecision(true, previousStatus, reason);
    }

    /**
     * スキップする判定結果を作成する。
     *
     * @param previousStatus 直近実行のステータス
     * @param reason 判定理由
     * @return スキップする判定結果
     */
    public static ExecutionDecision skip(ExecutionStatus previousStatus, String reason) {
        return new ExecutionDecision(false, previousStatus, reason);
    }
}
