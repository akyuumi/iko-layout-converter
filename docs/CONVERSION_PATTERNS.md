# CONVERSION PATTERNS

## 基本方針

変換仕様の正はマッピング定義書とする。

本ツールでは、大量データの変換は原則 SQL で実施する。Java は Task 実行制御、設定、ログ、再実行制御、SQL に渡す安全な識別子や定型 SQL 式の補助に限定する。

## 実装境界

| ID | 内容 | 主な実装場所 | 方針 |
| --- | --- | --- | --- |
| CV001 | 単純コピー | SQL | `SELECT SRC_COL` の形で Mapper XML に記述する |
| CV002 | 連結 | SQL | Oracle の `||` で連結する |
| CV003 | 0埋め | SQL | `LPAD` を使用する |
| CV004 | 型変換 | SQL | `TO_NUMBER`、`TO_DATE`、`CAST` などを使用する |
| CV005 | コード変換 | SQL | コード変換テーブル JOIN または `CASE` を使用する |
| CV006 | 条件分岐 | SQL | `CASE WHEN` を使用する |
| CV007 | 固定値 | SQL | SQL リテラルまたは bind 変数を使用する |
| CV008 | 集約 | SQL | `GROUP BY`、集約関数を使用する |
| CV009 | 縦化 | SQL | `UNION ALL` または Oracle の `UNPIVOT` を使用する |
| CV010 | 算術演算 | SQL | Oracle の算術式を使用する |

## Java 共通部品

- `ConversionPattern`
  - CV001〜CV010 の ID と責務境界を管理する。
- `SqlConversionExpressions`
  - Mapper XML や SQL 組み立てで使う定型 SQL 式を生成する。
  - 業務変換ロジックそのものは保持しない。
- `SchemaNames`
  - スキーマ名、テーブル名など SQL 識別子を検証してから修飾名を作る。

## 禁止事項

- 大量データを Java の List に全件読み込んで変換する。
- Task 内に業務変換ロジックを直接大量に書く。
- マッピング定義書にない変換仕様を Java 側で暗黙に追加する。
- 外部入力由来のスキーマ名、テーブル名、列名を未検証のまま `${}` に渡す。

