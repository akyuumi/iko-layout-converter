package jp.co.iko.layoutconverter.common;

/**
 * スキーマ名、テーブル名、列名など SQL 識別子を扱うユーティリティ。
 *
 * <p>MyBatis XML の {@code ${}} に渡す識別子は bind 変数化できないため、ここで形式を検証する。</p>
 */
public final class SchemaNames {

    private static final String IDENTIFIER = "[A-Za-z][A-Za-z0-9_#$]*";
    private static final String IDENTIFIER_PATH = IDENTIFIER + "(\\." + IDENTIFIER + ")*";

    private SchemaNames() {
    }

    /**
     * テーブル名を必要に応じてスキーマ修飾する。
     *
     * @param schema スキーマ名。未指定の場合はテーブル名のみ返す
     * @param tableName テーブル名
     * @return スキーマ修飾済み、または未修飾のテーブル名
     */
    public static String qualify(String schema, String tableName) {
        requireSqlIdentifier(tableName);
        if (schema == null || schema.isBlank()) {
            return tableName;
        }
        requireSqlIdentifier(schema);
        return schema + "." + tableName;
    }

    /**
     * 単一の SQL 識別子を検証する。
     *
     * @param identifier 検証対象
     * @return 検証済みの識別子
     */
    public static String requireSqlIdentifier(String identifier) {
        if (identifier == null || !identifier.matches(IDENTIFIER)) {
            throw new IllegalArgumentException("Invalid SQL identifier: " + identifier);
        }
        return identifier;
    }

    /**
     * ドット区切りの SQL 識別子パスを検証する。
     *
     * @param identifierPath 検証対象。例: {@code S1.COLUMN_NAME}
     * @return 検証済みの識別子パス
     */
    public static String requireSqlIdentifierPath(String identifierPath) {
        if (identifierPath == null || !identifierPath.matches(IDENTIFIER_PATH)) {
            throw new IllegalArgumentException("Invalid SQL identifier path: " + identifierPath);
        }
        return identifierPath;
    }
}
