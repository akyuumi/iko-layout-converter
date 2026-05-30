package jp.co.iko.layoutconverter.audit;

/**
 * Task 実行状態。
 *
 * <p>{@code EXECUTION_CONTROL.STATUS} に保存する値と一致させる。</p>
 */
public enum ExecutionStatus {
    /** 実行中。 */
    RUNNING,
    /** 正常終了。 */
    SUCCESS,
    /** 異常終了。 */
    ERROR,
    /** 実行制御によりスキップ。 */
    SKIPPED
}
