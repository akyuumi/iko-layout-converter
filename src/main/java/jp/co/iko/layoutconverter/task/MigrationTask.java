package jp.co.iko.layoutconverter.task;

/**
 * 移行先テーブル1つに対応する移行 Task の共通インターフェース。
 *
 * <p>実装クラスは Task ID を持ち、Mapper XML に定義された SQL を呼び出して変換処理を行う。</p>
 */
public interface MigrationTask {

    /**
     * Task ID を取得する。
     *
     * @return Task ID
     */
    String taskId();

    /**
     * 移行処理を実行する。
     *
     * @param context 実行コンテキスト
     * @return 処理結果
     */
    MigrationTaskResult execute(MigrationTaskContext context);
}
