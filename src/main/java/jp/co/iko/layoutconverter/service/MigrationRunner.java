package jp.co.iko.layoutconverter.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jp.co.iko.layoutconverter.audit.ExecutionControlService;
import jp.co.iko.layoutconverter.audit.ExecutionDecision;
import jp.co.iko.layoutconverter.config.MigrationProperties;
import jp.co.iko.layoutconverter.task.MigrationTask;
import jp.co.iko.layoutconverter.task.MigrationTaskContext;
import jp.co.iko.layoutconverter.task.MigrationTaskRegistry;
import jp.co.iko.layoutconverter.task.MigrationTaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 移行処理全体の起動制御を行う Runner。
 *
 * <p>起動引数または設定値から実行対象 Task を解決し、実行制御テーブルを更新しながら順次実行する。</p>
 */
@Component
public class MigrationRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MigrationRunner.class);

    private final MigrationProperties properties;
    private final MigrationTaskRegistry taskRegistry;
    private final ExecutionControlService executionControlService;

    /**
     * 移行 Runner を生成する。
     *
     * @param properties 移行設定
     * @param taskRegistry Task レジストリ
     * @param executionControlService 実行制御サービス
     */
    public MigrationRunner(
            MigrationProperties properties,
            MigrationTaskRegistry taskRegistry,
            ExecutionControlService executionControlService
    ) {
        this.properties = properties;
        this.taskRegistry = taskRegistry;
        this.executionControlService = executionControlService;
    }

    /**
     * Spring Boot 起動後に移行 Task を実行する。
     *
     * @param args 起動引数。{@code --tasks=TASK1,TASK2} を指定できる
     */
    @Override
    public void run(String... args) {
        List<String> requestedTaskIds = resolveTaskIds(args);
        List<MigrationTask> tasks = resolveTasks(requestedTaskIds);
        if (tasks.isEmpty()) {
            log.warn("実行対象の移行タスクが見つかりません。requestedTaskIds={}", requestedTaskIds);
            return;
        }

        String runId = UUID.randomUUID().toString();
        MigrationTaskContext context = new MigrationTaskContext(
                runId,
                properties.getSourceSchema(),
                properties.getTargetSchema(),
                properties.getCommitSize()
        );

        log.info("移行処理を開始しました。runId={} taskCount={} commitSize={}",
                runId, tasks.size(), properties.getCommitSize());

        for (MigrationTask task : tasks) {
            executeTask(task, context);
        }

        log.info("移行処理を終了しました。runId={}", runId);
    }

    private List<String> resolveTaskIds(String[] args) {
        List<String> taskIds = new ArrayList<>();
        for (String arg : args) {
            if (arg.startsWith("--tasks=")) {
                String value = arg.substring("--tasks=".length());
                for (String taskId : value.split(",")) {
                    if (StringUtils.hasText(taskId)) {
                        taskIds.add(taskId.trim());
                    }
                }
            }
        }
        if (taskIds.isEmpty()) {
            taskIds.addAll(properties.getTasks());
        }
        return taskIds;
    }

    private List<MigrationTask> resolveTasks(List<String> requestedTaskIds) {
        if (requestedTaskIds.isEmpty()) {
            return new ArrayList<>(taskRegistry.findAll());
        }

        List<MigrationTask> tasks = new ArrayList<>();
        for (String taskId : requestedTaskIds) {
            MigrationTask task = taskRegistry.findByTaskId(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("未登録の移行タスク ID です: " + taskId));
            tasks.add(task);
        }
        return tasks;
    }

    private void executeTask(MigrationTask task, MigrationTaskContext context) {
        Instant startedAt = Instant.now();
        ExecutionDecision decision = executionControlService.prepare(context.runId(), task.taskId());
        if (!decision.executable()) {
            return;
        }

        log.info("タスクを開始しました。runId={} taskId={}", context.runId(), task.taskId());
        try {
            MigrationTaskResult result = task.execute(context);
            executionControlService.markSuccess(context.runId(), task.taskId());
            Duration elapsed = Duration.between(startedAt, Instant.now());
            log.info("タスクを終了しました。runId={} taskId={} readCount={} convertedCount={} errorCount={} elapsedMillis={}",
                    context.runId(),
                    task.taskId(),
                    result.readCount(),
                    result.convertedCount(),
                    result.errorCount(),
                    elapsed.toMillis());
        } catch (RuntimeException e) {
            executionControlService.markError(context.runId(), task.taskId());
            Duration elapsed = Duration.between(startedAt, Instant.now());
            log.error("タスクが失敗しました。runId={} taskId={} elapsedMillis={}",
                    context.runId(), task.taskId(), elapsed.toMillis(), e);
            throw e;
        }
    }
}
