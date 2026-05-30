package jp.co.iko.layoutconverter.audit;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 実行制御テーブル {@code EXECUTION_CONTROL} にアクセスする MyBatis Mapper。
 *
 * <p>テーブル名はスキーマ修飾済みの識別子を呼び出し元で検証してから渡す。</p>
 */
@Mapper
public interface ExecutionControlMapper {

    /**
     * 指定 Task の直近実行レコードを取得する。
     *
     * @param executionControlTable スキーマ修飾済みの実行制御テーブル名
     * @param taskId Task ID
     * @return 直近実行レコード。存在しない場合は {@code null}
     */
    ExecutionControlRecord findLatest(
            @Param("executionControlTable") String executionControlTable,
            @Param("taskId") String taskId
    );

    /**
     * Task 開始状態を {@code RUNNING} として登録する。
     *
     * @param executionControlTable スキーマ修飾済みの実行制御テーブル名
     * @param runId 実行 ID
     * @param taskId Task ID
     * @return 登録件数
     */
    int insertRunning(
            @Param("executionControlTable") String executionControlTable,
            @Param("runId") String runId,
            @Param("taskId") String taskId
    );

    /**
     * 実行制御レコードの状態と終了日時を更新する。
     *
     * @param executionControlTable スキーマ修飾済みの実行制御テーブル名
     * @param runId 実行 ID
     * @param taskId Task ID
     * @param status 更新後ステータス
     * @return 更新件数
     */
    int updateStatus(
            @Param("executionControlTable") String executionControlTable,
            @Param("runId") String runId,
            @Param("taskId") String taskId,
            @Param("status") ExecutionStatus status
    );
}
