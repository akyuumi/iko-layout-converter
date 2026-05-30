package jp.co.iko.layoutconverter.conversion;

import java.util.Arrays;
import java.util.stream.Collectors;
import jp.co.iko.layoutconverter.common.SchemaNames;

/**
 * SQL で使う定型的な変換式を組み立てる補助クラス。
 *
 * <p>業務変換ロジックを隠蔽するためのものではなく、Mapper XML に渡す式の表記ゆれを抑えるために使う。</p>
 */
public final class SqlConversionExpressions {

    private SqlConversionExpressions() {
    }

    /**
     * 単純コピー用の列参照を検証して返す。
     *
     * @param column 列名またはエイリアス付き列名
     * @return 検証済みの列参照
     */
    public static String copy(String column) {
        return SchemaNames.requireSqlIdentifierPath(column);
    }

    /**
     * Oracle の文字列連結式を作成する。
     *
     * @param expressions 連結対象の SQL 式
     * @return {@code ||} で連結した SQL 式
     */
    public static String concat(String... expressions) {
        if (expressions.length == 0) {
            throw new IllegalArgumentException("Concat expressions must not be empty.");
        }
        return Arrays.stream(expressions)
                .map(SqlConversionExpressions::requireExpression)
                .collect(Collectors.joining(" || "));
    }

    /**
     * Oracle の {@code LPAD} による 0 埋め式を作成する。
     *
     * @param expression 対象 SQL 式
     * @param length 桁数
     * @return 0 埋め SQL 式
     */
    public static String zeroPad(String expression, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Zero pad length must be greater than zero.");
        }
        return "LPAD(" + requireExpression(expression) + ", " + length + ", '0')";
    }

    /**
     * 文字列固定値の SQL リテラルを作成する。
     *
     * @param value 固定値
     * @return シングルクォートをエスケープした SQL リテラル
     */
    public static String fixedString(String value) {
        return "'" + value.replace("'", "''") + "'";
    }

    /**
     * Oracle の {@code TO_NUMBER} 式を作成する。
     *
     * @param expression 対象 SQL 式
     * @return 数値変換 SQL 式
     */
    public static String toNumber(String expression) {
        return "TO_NUMBER(" + requireExpression(expression) + ")";
    }

    /**
     * Oracle の {@code NVL} 式を作成する。
     *
     * @param expression 対象 SQL 式
     * @param defaultExpression NULL 時の既定 SQL 式
     * @return NULL 置換 SQL 式
     */
    public static String nvl(String expression, String defaultExpression) {
        return "NVL(" + requireExpression(expression) + ", " + requireExpression(defaultExpression) + ")";
    }

    private static String requireExpression(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("SQL expression must not be blank.");
        }
        return expression.trim();
    }
}
