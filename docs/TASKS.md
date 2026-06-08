# TASKS

## Completed

- [x] Spring Boot / MyBatis ベースの実行基盤を作成する
  - 完了条件:
    - Java 21 / Gradle / Spring Boot の最小構成がある
    - `MigrationRunner` が起動引数から Task を実行できる
    - `MigrationTask` と Task レジストリがある
    - MyBatis Mapper / XML SQL 外出しの配置規約がある
    - 設定ファイルでコミット件数・スキーマ・タスクを指定できる
    - ビルドまたはコンパイル確認ができる

- [x] `EXECUTION_CONTROL` を使った実行制御を実装する
  - 完了条件:
    - RUN_ID / TASK_ID / STATUS / START_AT / END_AT を更新できる
    - 実行中断時の再実行判断ができる
    - 失敗時に ERROR として記録される

- [x] サンプル変換 Task を 1 件実装する
  - 完了条件:
    - 移行先テーブル1つにつき1Task の構成例がある
    - Mapper XML に INSERT または MERGE SQL がある
    - 件数ログを出力する

- [x] 変換パターンの共通部品を整理する
  - 完了条件:
    - CV001〜CV010 の実装方針が文書化される
    - Java 実装対象と SQL 実装対象の境界が明確になる

- [x] Oracle 接続前提の設定例を追加する
  - 完了条件:
    - datasource 設定例がある
    - ローカル検証用の設定分離方針がある

- [x] JavaDoc を日本語で追加する
  - 完了条件:
    - 公開クラス・インターフェース・record・enum に日本語 JavaDoc がある
    - 主要な public メソッドに日本語 JavaDoc がある
    - テストが成功する

## Backlog
