# iko-layout-converter SPEC

⸻

要件定義書

現行→次期データ変換ツール

⸻

1. 目的

1.1 背景

現行システムはメインフレーム（COBOL）で稼働している。

次期システムはJava＋Oracleで構築される。

現行データを次期データ構造へ変換し、次期システムへ移行する必要がある。

⸻

1.2 本ツールの目的

現行Oracleスキーマに格納されたデータを参照し、マッピング定義書に従って次期データへ変換して次期スキーマへ登録する。

対象工程：

COBOL
↓
TSV変換
↓
SQL\*Loader
↓
現行Oracle
↓ ← 本ツール対象
変換
↓
次期Oracle
↓
業務反映

⸻

2. スコープ

対象

- 現行スキーマ読込
- データ変換
- 次期スキーマ登録
- 実行制御
- ログ出力
- 再実行制御

⸻

対象外

- TSV生成
- SQL\*Loader
- 次期DB反映
- JP1ジョブ設計
- マッピング定義書作成

⸻

3. 基本方針

3.1 設計思想

本ツールはマッピング定義書を変換仕様の正（Single Source of Truth）とする。

実装コードはマッピング定義書に従属する。

マッピング定義書
↓
SQL
↓
Java制御
↓
次期データ

⸻

3.2 実装単位

原則：

# 1次期テーブル

# 1マッピング定義書

# 1変換Task

1Mapper

想定規模：

項目 想定
次期テーブル 約100
マッピング定義書 約100

⸻

4. 前提条件

4.1 現行データ

データ構造

現行システムはCOBOLデータセットをOracleへ取り込んでいる。

COBOL属性 内容
9 外部10進
PACK-9 内部10進
COMP-9 バイナリ
X EBCDIC
N JEF

⸻

テーブル分割

Oracle列制約回避のため900列単位で分割済。

例：

TABLE_001
TABLE_002
TABLE_003

結合条件：

KEY一致

⸻

4.2 次期データ

OCCURS変換

現行：

KEY OCCURS1 OCCURS2 OCCURS3

次期：

KEY OCCURS
KEY OCCURS
KEY OCCURS

変換：

横持ち
↓
縦持ち

空項目除外可否は設定可能とする。

⸻

5. マッピング定義書

5.1 概要

次期テーブル定義をベースに、移行元および変換方法を記載する。

⸻

5.2 記載項目

項目 説明
次期テーブル
次期項目
型
移行元テーブル
移行元項目
変換方式
条件
備考

例：

次期項目 移行元 変換
BIRTHDAY YY MM DD 連結
AMOUNT KINGAKU 0埋め
NEXT_CODE OLD_CODE コード変換

⸻

5.3 変換パターン

対応対象：

ID 内容
CV001 単純コピー
CV002 連結
CV003 0埋め
CV004 型変換
CV005 コード変換
CV006 条件分岐
CV007 固定値
CV008 集約
CV009 縦化
CV010 算術演算

変換追加可能な構造とする。

⸻

6. 機能要件

F001 読込

入力：

現行スキーマ

要件：

- ストリーム読込
- ページング可能
- 全件保持禁止

⸻

F002 結合

対象：

TABLE001
～
TABLE050

方式：

LEFT JOIN

制約：

- NULL許容
- 欠損ログ出力

⸻

F003 変換

原則：

大量変換はSQLで実施。

対象：

- 集約
- JOIN
- コード変換
- OCCURS展開
- MERGE

Java側責務：

- 制御
- エラー処理
- ログ

⸻

F004 登録

登録先：

NEXT_SCHEMA

方式：

INSERT
MERGE

コミット：

10000件単位

設定変更可能。

⸻

7. 非機能要件

性能

項目 値
最大メモリ 40GB
目標時間 8時間以内
同時実行 JP1制御

⸻

可用性

要件：

- 冪等性
- 中断再開
- リトライ可能

制御テーブル：

EXECUTION_CONTROL

項目：

項目

RUN_ID

TASK_ID

STATUS

START_AT

END_AT

⸻

ログ

出力：

INFO
WARN
ERROR

記録：

- 処理件数
- 変換件数
- 所要時間
- 異常件数

⸻

8. 技術要件

項目 内容
Java 21
FW Spring Boot
利用範囲 DI・設定・TX・ログ
ORM 使用しない
DBアクセス MyBatis
DB Oracle
ジョブ JP1
ビルド Gradle

制約：

Spring Batch禁止
JPA禁止

⸻

9. アーキテクチャ

JP1
↓
Spring Boot
↓
MigrationRunner
↓
MigrationTask
↓
MyBatis Mapper
↓
Oracle SQL
↓
Next Schema

構成：

task/
mapper/
service/
audit/
config/
common/

⸻

10. 実装ルール（Codex向け）

必須：

- マッピング定義書を仕様の正とする
- 1次期テーブル=1Task
- SQL外出し
- 共通変換部品化
- 全件読込禁止
- 再実行可能

禁止：

- Spring Batch
- JPA
- 巨大Service
- SQL直書き
- static状態管理
- Mapper肥大化

⸻

11. 検討課題

ID 課題
P001 大量JOIN性能
P002 縦化による行爆発
P003 変換仕様変更時の追従
P004 再実行単位
P005 コミット粒度

⸻
