# DECISIONS

## 2026-05-23: 初期構成は Spring Boot CLI アプリケーションとして実装

決定事項:

- Web アプリケーションではなく、JP1 から起動される CLI 型 Spring Boot アプリケーションとして構成する。
- 起動引数または設定値で実行 Task を指定する。

理由:

- SPEC.md のアーキテクチャが `JP1 -> Spring Boot -> MigrationRunner -> MigrationTask` を前提としている。
- バッチ制御は独自の実行制御テーブルで扱い、Spring Batch は禁止されている。

影響:

- `CommandLineRunner` を入口にする。
- HTTP API や画面は初期スコープに含めない。

制約:

- Spring Batch と JPA は使用しない。
- SQL は Mapper XML に外出しする。

## 2026-05-23: 実行 Task は `MigrationTask` インターフェースで分離

決定事項:

- 1次期テーブル=1Task の方針に合わせ、各変換処理は `MigrationTask` 実装として登録する。
- `taskId` をキーに `MigrationRunner` が対象 Task を選択する。

理由:

- 約100テーブル規模でも Task 単位で責務を分けられる。
- JP1 や再実行制御と Task ID の対応を取りやすい。

影響:

- 新規テーブル変換は Task クラスと Mapper XML を追加する形になる。

制約:

- 共通処理は service / audit / common に置き、Task を巨大化させない。

## 2026-05-23: `EXECUTION_CONTROL` は Task 単位の最新状態で再実行判断する

決定事項:

- `EXECUTION_CONTROL` は `RUN_ID` と `TASK_ID` を主キーとし、実行ごとの状態を記録する。
- Task 開始時に `RUNNING` を登録し、正常終了時は `SUCCESS`、異常終了時は `ERROR`、完了済みスキップ時は `SKIPPED` に更新する。
- 直近状態が `SUCCESS` の Task は再実行時にスキップする。
- 直近状態が `RUNNING` または `ERROR` の Task は再実行可能とする。

理由:

- SPEC.md の冪等性、中断再開、リトライ可能の要件を最小構成で満たすため。
- JP1 から Task 単位で再実行する運用に合わせやすいため。

影響:

- 完了済み Task を意図的に再実行する仕組みは未実装。
- 再実行対象の判定は Task ID ごとの最新 `START_AT` を基準にする。

制約:

- `EXECUTION_CONTROL` は次期スキーマ側に配置する前提とする。
- 詳細な再実行範囲制御は今後の Task 実装時に必要に応じて追加する。

## 2026-05-26: サンプル変換 Task は参照実装として `SAMPLE_CUSTOMER` を用意

決定事項:

- 実業務のマッピング定義書が未配置のため、参照実装として `SAMPLE_CUSTOMER` Task を追加する。
- 現行側の分割テーブル `SAMPLE_CUSTOMER_001` / `SAMPLE_CUSTOMER_002` を LEFT JOIN し、次期側 `SAMPLE_CUSTOMER` へ MERGE する。

理由:

- 1次期テーブル=1Task、SQL外出し、件数ログ出力の実装パターンをリポジトリ上で確認できるようにするため。
- 今後の実マッピング Task 追加時の雛形として使えるため。

影響:

- `SAMPLE_CUSTOMER` は業務仕様ではなく開発用サンプルである。
- 実マッピング定義書が追加されたら、同じ構成で実 Task を追加する。

制約:

- サンプル DDL は検証用途であり、本番 DB 定義ではない。

## 2026-05-26: 変換パターンは SQL 主体、Java は補助に限定

決定事項:

- CV001〜CV010 は原則 SQL で実装する。
- Java の共通部品は変換 ID 管理、SQL 式の補助、SQL 識別子バリデーションに限定する。
- CV001〜CV010 の実装方針は `docs/CONVERSION_PATTERNS.md` に記録する。

理由:

- SPEC.md が大量変換を SQL で実施する方針を定めているため。
- Java 側に業務変換ロジックを持たせると、全件読込禁止とマッピング定義書追従の方針に反しやすいため。
- Mapper XML の `${}` に渡すスキーマ名、テーブル名、列名は検証が必要なため。

影響:

- 実 Task は Mapper XML に変換 SQL を記述する。
- Java の `SqlConversionExpressions` は定型 SQL 式の生成補助として使う。
- `SchemaNames.qualify` は不正な SQL 識別子を拒否する。

制約:

- 複雑な SQL 式はマッピング定義書と Mapper XML を優先し、Java に隠蔽しない。
- 今後 Java 実装が必要な変換が出た場合は、個別に理由を DECISIONS.md へ追記する。

## 2026-05-26: Oracle 接続設定は profile ごとに分離

決定事項:

- `application.yml` は共通設定のみを持つ。
- ローカル検証用の Oracle 接続例は `application-local.yml` に置く。
- 実 DB 接続用の環境変数必須設定は `application-oracle.yml` に置く。
- 接続手順と環境変数は `docs/ORACLE_CONNECTION.md` に記録する。

理由:

- ローカル検証と JP1 実行時の接続情報を混在させないため。
- DB パスワードなどの秘密情報をリポジトリに固定しないため。
- Oracle 前提の設定例を残しつつ、環境ごとの上書きを明確にするため。

影響:

- 実行時は `--spring.profiles.active=local` または `--spring.profiles.active=oracle` を指定する。
- `oracle` profile では `IKO_DB_URL`、`IKO_DB_USER`、`IKO_DB_PASSWORD`、`IKO_CURRENT_SCHEMA`、`IKO_NEXT_SCHEMA` が必須になる。

制約:

- ローカル検証も Oracle 接続を前提とし、H2 等の別 DB 互換検証は行わない。
