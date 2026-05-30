package jp.co.iko.layoutconverter.task.sample;

import jp.co.iko.layoutconverter.common.SchemaNames;
import jp.co.iko.layoutconverter.task.MigrationTask;
import jp.co.iko.layoutconverter.task.MigrationTaskContext;
import jp.co.iko.layoutconverter.task.MigrationTaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * サンプル顧客テーブルを移行する参照実装 Task。
 *
 * <p>分割された現行テーブルを LEFT JOIN し、次期テーブルへ MERGE する実装例を示す。</p>
 */
@Component
public class SampleCustomerMigrationTask implements MigrationTask {

    /** サンプル顧客変換 Task の Task ID。 */
    public static final String TASK_ID = "SAMPLE_CUSTOMER";

    private static final Logger log = LoggerFactory.getLogger(SampleCustomerMigrationTask.class);
    private static final String SOURCE_TABLE_001 = "SAMPLE_CUSTOMER_001";
    private static final String SOURCE_TABLE_002 = "SAMPLE_CUSTOMER_002";
    private static final String TARGET_TABLE = "SAMPLE_CUSTOMER";

    private final SampleCustomerMapper mapper;

    /**
     * サンプル顧客変換 Task を生成する。
     *
     * @param mapper サンプル顧客 Mapper
     */
    public SampleCustomerMigrationTask(SampleCustomerMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * サンプル顧客変換 Task の Task ID を返す。
     *
     * @return {@value #TASK_ID}
     */
    @Override
    public String taskId() {
        return TASK_ID;
    }

    /**
     * サンプル顧客変換を実行する。
     *
     * @param context 実行コンテキスト
     * @return 読込件数と MERGE 件数を含む処理結果
     */
    @Override
    @Transactional
    public MigrationTaskResult execute(MigrationTaskContext context) {
        String sourceTable001 = SchemaNames.qualify(context.currentSchema(), SOURCE_TABLE_001);
        String sourceTable002 = SchemaNames.qualify(context.currentSchema(), SOURCE_TABLE_002);
        String targetTable = SchemaNames.qualify(context.nextSchema(), TARGET_TABLE);

        long readCount = mapper.countSource(sourceTable001);
        int convertedCount = mapper.mergeCustomers(sourceTable001, sourceTable002, targetTable);

        log.info("Sample customer migration merged rows. taskId={} readCount={} convertedCount={} commitSize={}",
                taskId(), readCount, convertedCount, context.commitSize());

        return new MigrationTaskResult(readCount, convertedCount, 0);
    }
}
