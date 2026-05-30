package jp.co.iko.layoutconverter.conversion;

import java.util.Arrays;

/**
 * マッピング定義書で使用する変換パターン。
 *
 * <p>CV001〜CV010 の ID と説明、主な実装責務を管理する。</p>
 */
public enum ConversionPattern {
    /** 単純コピー。 */
    CV001("単純コピー", ConversionResponsibility.SQL),
    /** 複数項目の連結。 */
    CV002("連結", ConversionResponsibility.SQL),
    /** 指定桁数への 0 埋め。 */
    CV003("0埋め", ConversionResponsibility.SQL),
    /** 文字列、数値、日付などの型変換。 */
    CV004("型変換", ConversionResponsibility.SQL),
    /** コード変換テーブルや条件式によるコード変換。 */
    CV005("コード変換", ConversionResponsibility.SQL),
    /** 条件に応じた値の分岐。 */
    CV006("条件分岐", ConversionResponsibility.SQL),
    /** 固定値の設定。 */
    CV007("固定値", ConversionResponsibility.SQL),
    /** 集約関数や GROUP BY による集約。 */
    CV008("集約", ConversionResponsibility.SQL),
    /** OCCURS など横持ち項目の縦化。 */
    CV009("縦化", ConversionResponsibility.SQL),
    /** 加減乗除などの算術演算。 */
    CV010("算術演算", ConversionResponsibility.SQL);

    private final String description;
    private final ConversionResponsibility responsibility;

    ConversionPattern(String description, ConversionResponsibility responsibility) {
        this.description = description;
        this.responsibility = responsibility;
    }

    /**
     * 変換パターン ID を取得する。
     *
     * @return 例: {@code CV001}
     */
    public String id() {
        return name();
    }

    /**
     * 変換パターンの説明を取得する。
     *
     * @return 日本語の説明
     */
    public String description() {
        return description;
    }

    /**
     * 主な実装責務を取得する。
     *
     * @return 実装責務
     */
    public ConversionResponsibility responsibility() {
        return responsibility;
    }

    /**
     * 変換パターン ID から enum を取得する。
     *
     * @param id 変換パターン ID
     * @return 対応する変換パターン
     */
    public static ConversionPattern fromId(String id) {
        return Arrays.stream(values())
                .filter(pattern -> pattern.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown conversion pattern id: " + id));
    }
}
