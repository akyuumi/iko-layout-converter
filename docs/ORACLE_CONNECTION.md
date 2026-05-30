# ORACLE CONNECTION

## 方針

`application.yml` は共通設定のみを持つ。Oracle の接続情報は profile ごとの設定ファイルに分離する。

| Profile | 用途 | 設定ファイル |
| --- | --- | --- |
| `local` | ローカル Oracle Free / 開発 DB での検証 | `application-local.yml` |
| `oracle` | JP1 などから起動する実 DB 接続 | `application-oracle.yml` |

## ローカル検証

ローカル検証では `local` profile を使う。

```bash
gradle bootRun --args='--spring.profiles.active=local --tasks=SAMPLE_CUSTOMER'
```

既定値:

| 項目 | 値 |
| --- | --- |
| URL | `jdbc:oracle:thin:@localhost:1521/FREEPDB1` |
| User | `CURRENT_SCHEMA` |
| Password | `password` |
| Current schema | `CURRENT_SCHEMA` |
| Next schema | `NEXT_SCHEMA` |
| Task | `SAMPLE_CUSTOMER` |

必要に応じて環境変数で上書きする。

```bash
export IKO_DB_URL='jdbc:oracle:thin:@localhost:1521/FREEPDB1'
export IKO_DB_USER='CURRENT_SCHEMA'
export IKO_DB_PASSWORD='password'
export IKO_CURRENT_SCHEMA='CURRENT_SCHEMA'
export IKO_NEXT_SCHEMA='NEXT_SCHEMA'
export IKO_TASKS='SAMPLE_CUSTOMER'
```

## Oracle 接続例

実 DB 接続では `oracle` profile を使う。接続情報とスキーマ名は環境変数で必ず渡す。

```bash
export IKO_DB_URL='jdbc:oracle:thin:@db-host:1521/SERVICE_NAME'
export IKO_DB_USER='MIGRATION_USER'
export IKO_DB_PASSWORD='password'
export IKO_CURRENT_SCHEMA='CURRENT_SCHEMA'
export IKO_NEXT_SCHEMA='NEXT_SCHEMA'
export IKO_TASKS='SAMPLE_CUSTOMER'

gradle bootRun --args='--spring.profiles.active=oracle'
```

## 必須環境変数

| 環境変数 | 内容 |
| --- | --- |
| `IKO_DB_URL` | Oracle JDBC URL |
| `IKO_DB_USER` | DB ユーザー |
| `IKO_DB_PASSWORD` | DB パスワード |
| `IKO_CURRENT_SCHEMA` | 現行スキーマ名 |
| `IKO_NEXT_SCHEMA` | 次期スキーマ名 |

## 任意環境変数

| 環境変数 | 既定値 | 内容 |
| --- | --- | --- |
| `IKO_TASKS` | 空 | 実行 Task ID。カンマ区切り |
| `IKO_COMMIT_SIZE` | `10000` | コミット件数 |
| `IKO_DB_POOL_SIZE` | `4` | HikariCP 最大プールサイズ |
| `IKO_DB_MIN_IDLE` | `0` | HikariCP 最小 idle 数 |
| `IKO_DB_CONNECTION_TIMEOUT_MS` | `30000` | 接続タイムアウト |
| `IKO_DB_VALIDATION_TIMEOUT_MS` | `5000` | 検証タイムアウト |
| `IKO_DB_MAX_LIFETIME_MS` | `1800000` | 接続最大 lifetime |

## 事前 DDL

実行制御テーブルは次期スキーマに作成する。

```bash
src/main/resources/db/oracle/execution_control.sql
```

サンプル Task を動かす場合は検証用 DDL を使用する。

```bash
src/main/resources/db/oracle/sample_customer.sql
```

