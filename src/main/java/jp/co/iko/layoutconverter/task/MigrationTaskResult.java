package jp.co.iko.layoutconverter.task;

/**
 * Task の処理結果。
 *
 * @param readCount 読込件数
 * @param convertedCount 変換または登録件数
 * @param errorCount 異常件数
 */
public record MigrationTaskResult(
        long readCount,
        long convertedCount,
        long errorCount
) {

    /**
     * 件数がすべて 0 の結果を返す。
     *
     * @return 空の処理結果
     */
    public static MigrationTaskResult empty() {
        return new MigrationTaskResult(0, 0, 0);
    }
}
