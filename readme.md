# 🍜 Sapporo Ramen — ミニ予約管理システム

札幌ラーメン店向けのフルスタック予約管理システムです。テーブル予約・メニュー管理・注文管理・認証機能を備えています。

---

## 🛠 技術スタック

| レイヤー       | 技術                      |
| -------------- | ------------------------- |
| フロントエンド | React                     |
| バックエンド   | Java 21 / Spring Boot 3.4 |
| データベース   | PostgreSQL 15             |
| インフラ       | Docker / Docker Compose   |

---

## ✨ 主な機能

- 🔐 **認証機能** — ユーザー登録・ログイン・ロールベースのアクセス制御
- 📅 **テーブル予約** — 日時・人数・テーブル指定での予約管理
- 🍱 **メニュー管理** — メニューの追加・編集・削除
- 🧾 **注文管理** — 注文の作成・ステータス管理・履歴確認

---

## ⚙️ 動作環境

以下のツールのみ必要です：

- [Docker Desktop](https://www.docker.com/products/docker-desktop)

Java・Node.js・PostgreSQL などの個別インストールは**不要**です。

---

## 🚀 起動手順

### 1. リポジトリをクローン

```bash
git clone --recurse-submodules https://github.com/your-username/demo-mini-booking-system.git
cd demo-mini-booking-system
```

### 2. 起動

```bash
docker compose up --build
```

初回はイメージのビルドに数分かかります。

### 3. アクセス

| サービス         | URL                   |
| ---------------- | --------------------- |
| フロントエンド   | http://localhost:3000 |
| バックエンド API | http://localhost:8080 |
| PostgreSQL       | localhost:5432        |

---

## 🔑 テスト用アカウント

| ロール       | メールアドレス  | パスワード |
| ------------ | --------------- | ---------- |
| 管理者       | admin@ramen.com | admin123   |
| 一般ユーザー | user@ramen.com  | user123    |

---

## 🗂 プロジェクト構成

```
demo-mini-booking-system/
├── Backend/               # Spring Boot アプリケーション
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── FrontEnd/              # React アプリケーション
│   ├── src/
│   ├── package.json
│   └── Dockerfile
└── docker-compose.yml     # 全サービスの統合設定
```

---

## 🛑 停止方法

```bash
# 停止（データ保持）
docker compose down

# 停止＋データ削除
docker compose down -v
```

---

## 📝 備考

- データベースのデータは Docker Volume で永続化されています
- 環境変数は `docker-compose.yml` で管理しています
- 本プロジェクトはデモ用途のため、本番環境への適用には追加のセキュリティ設定が必要です
