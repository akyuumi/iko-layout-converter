# CHANGELOG

## 2026-05-23

### Added

- プロジェクト管理用の `TASKS.md`、`DECISIONS.md`、`CHANGELOG.md` を追加。
- Java 21 / Gradle / Spring Boot / MyBatis の最小プロジェクト構成を追加。
- `MigrationRunner`、`MigrationTask`、Task レジストリ、実行コンテキスト、実行結果モデルを追加。
- `application.yml` に移行元スキーマ、移行先スキーマ、コミット件数、実行 Task の設定を追加。
- Mapper XML 外出し用の `src/main/resources/mapper` 配置を追加。
- `.gitignore` を追加。
- `EXECUTION_CONTROL` を使った Task 単位の実行制御を追加。
- `ExecutionControlMapper` と Mapper XML を追加。
- `EXECUTION_CONTROL` の Oracle DDL 例を追加。
- 実行制御 Service の単体テストを追加。
- サンプル変換 Task `SAMPLE_CUSTOMER` を追加。
- `SampleCustomerMapper` と Mapper XML の MERGE SQL を追加。
- サンプル変換用 Oracle DDL 例を追加。
- サンプル Task の単体テストを追加。
- CV001〜CV010 の実装方針をまとめた `docs/CONVERSION_PATTERNS.md` を追加。
- `ConversionPattern` と `ConversionResponsibility` を追加。
- SQL 定型式補助 `SqlConversionExpressions` を追加。
- SQL 識別子バリデーションを `SchemaNames` に追加。
- 変換共通部品の単体テストを追加。
- Oracle 接続手順をまとめた `docs/ORACLE_CONNECTION.md` を追加。
- ローカル検証用 profile `application-local.yml` を追加。
- 実 DB 接続用 profile `application-oracle.yml` を追加。
- 主要な Java クラス、record、enum、public メソッドに日本語 JavaDoc を追加。

### Changed

- `MigrationRunner` が Task 実行前後に実行制御テーブルを更新するよう変更。
- `SchemaNames.qualify` がスキーマ名とテーブル名を検証するよう変更。
- `application.yml` を共通設定のみに整理し、datasource 設定を profile 別ファイルへ分離。
