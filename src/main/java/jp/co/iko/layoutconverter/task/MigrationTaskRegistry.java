package jp.co.iko.layoutconverter.task;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Spring Bean として登録された {@link MigrationTask} を Task ID で管理するレジストリ。
 */
@Component
public class MigrationTaskRegistry {

    private final Map<String, MigrationTask> tasks;

    /**
     * 登録済み Task からレジストリを生成する。
     *
     * @param tasks Spring Bean として登録された Task 一覧
     */
    public MigrationTaskRegistry(Collection<MigrationTask> tasks) {
        this.tasks = new LinkedHashMap<>();
        for (MigrationTask task : tasks) {
            MigrationTask duplicated = this.tasks.putIfAbsent(task.taskId(), task);
            if (duplicated != null) {
                throw new IllegalStateException("Duplicated migration task id: " + task.taskId());
            }
        }
    }

    /**
     * Task ID に対応する Task を取得する。
     *
     * @param taskId Task ID
     * @return 対応する Task。存在しない場合は空
     */
    public Optional<MigrationTask> findByTaskId(String taskId) {
        return Optional.ofNullable(tasks.get(taskId));
    }

    /**
     * 登録済み Task を登録順に取得する。
     *
     * @return 登録済み Task
     */
    public Collection<MigrationTask> findAll() {
        return tasks.values();
    }
}
