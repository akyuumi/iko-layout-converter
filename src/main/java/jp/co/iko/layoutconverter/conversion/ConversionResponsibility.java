package jp.co.iko.layoutconverter.conversion;

/**
 * 変換パターンの主な実装責務。
 */
public enum ConversionResponsibility {
    /** SQL で変換する。 */
    SQL,
    /** Java で変換する。現時点では原則使用しない。 */
    JAVA
}
