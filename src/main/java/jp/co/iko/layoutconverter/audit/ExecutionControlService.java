package jp.co.iko.layoutconverter.audit;

import jp.co.iko.layoutconverter.common.SchemaNames;
import jp.co.iko.layoutconverter.config.MigrationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Task 単位の実行制御を扱うサービス。
 *
 * <p>直近の実行状態を参照し、実行可否の判断、開始登録、正常終了・異常終了の更新を行う。</p>
 */
@Service
public class ExecutionControlService {

    private static final Logger log = LoggerFactory.getLogger(ExecutionControlService.class);
    private static final String EXECUTION_CONTROL_TABLE = "EXECUTION_CONTROL";

    private final ExecutionControlMapper mapper;
    private final MigrationProperties properties;

    /**
     * 実行制御サービスを生成する。
     *
     * @param mapper 実行制御 Mapper
     * @param properties 移行設定
     */
    public ExecutionControlService(ExecutionControlMapper mapper, MigrationProperties properties) {
        this.mapper = mapper;
        this.properties = properties;
    }

    /**
     * Task 実行前の準備を行い、今回実行すべきかを判定する。
     *
     * <p>直近が {@code SUCCESS} の場合は今回の実行を {@code SKIPPED} として記録する。
     * 直近が {@code RUNNING} または {@code ERROR} の場合は再実行対象として扱う。</p>
     *
     * @param runId 今回の実行 ID
     * @param taskId 実行対象 Task ID
     * @return 実行可否と判定理由
     */
    public ExecutionDecision prepare(String runId, String taskId) {
        ExecutionControlRecord latest = mapper.findLatest(executionControlTable(), taskId);
        ExecutionDecision decision = decide(latest);
        if (!decision.executable()) {
            mapper.insertRunning(executionControlTable(), runId, taskId);
            mapper.updateStatus(executionControlTable(), runId, taskId, ExecutionStatus.SKIPPED);
            log.info("Task skipped by execution control. runId={} taskId={} previousStatus={} reason={}",
                    runId, taskId, decision.previousStatus(), decision.reason());
            return decision;
        }

        mapper.insertRunning(executionControlTable(), runId, taskId);
        if (decision.previousStatus() == ExecutionStatus.RUNNING) {
            log.warn("Previous execution may have been interrupted. runId={} taskId={} reason={}",
                    runId, taskId, decision.reason());
        }
        return decision;
    }

    /**
     * Task の正常終了を記録する。
     *
     * @param runId 実行 ID
     * @param taskId Task ID
     */
    public void markSuccess(String runId, String taskId) {
        updateStatus(runId, taskId, ExecutionStatus.SUCCESS);
    }

    /**
     * Task の異常終了を記録する。
     *
     * @param runId 実行 ID
     * @param taskId Task ID
     */
    public void markError(String runId, String taskId) {
        updateStatus(runId, taskId, ExecutionStatus.ERROR);
    }

    private ExecutionDecision decide(ExecutionControlRecord latest) {
        if (latest == null) {
            return ExecutionDecision.execute(null, "no previous execution");
        }
        if (latest.status() == ExecutionStatus.SUCCESS) {
            return ExecutionDecision.skip(latest.status(), "already completed");
        }
        if (latest.status() == ExecutionStatus.RUNNING) {
            return ExecutionDecision.execute(latest.status(), "previous execution was not closed");
        }
        return ExecutionDecision.execute(latest.status(), "previous execution is retryable");
    }

    private void updateStatus(String runId, String taskId, ExecutionStatus status) {
        int updated = mapper.updateStatus(executionControlTable(), runId, taskId, status);
        if (updated != 1) {
            throw new IllegalStateException("Failed to update execution control. runId="
                    + runId + " taskId=" + taskId + " status=" + status);
        }
    }

    private String executionControlTable() {
        return SchemaNames.qualify(properties.getNextSchema(), EXECUTION_CONTROL_TABLE);
    }
}
