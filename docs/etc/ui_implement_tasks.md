# FreezerMan UI 実装タスク一覧（細分化版）

## スレッド1（共通・導線準備）

### 1-A: ルーティング設計
- `FreezerManDestination` を定義
    - `StockList`, `StockAdd`, `StockEdit/{stockId}`, `CategoryEdit`, `LocationEdit`, `AboutApp`
- Route 文字列と引数仕様を確定
- NavHost のルート名だけ差し替え（Screen 本体は触らない）

### 1-B: 空 Screen 群の作成
- `CategoryEditScreen` / `LocationEditScreen` / `AboutAppScreen` など
    - 中身は `Text("WIP")`
- NavHost へ結線
- ViewModel / UseCase / DI には触らない

### 1-C: トップバー → ④⑤⑥ への遷移実装
- `StockListScreen` の AppBar 実装
    - OverflowMenu →
        - CategoryEdit
        - LocationEdit
        - AboutApp
          へ navigate
- ④⑤⑥ はまだ WIP のままで OK

---

## スレッド2（④カテゴリ編集画面）

### Domain
- `GetCategoriesUseCase`
- `UpsertCategoryUseCase`
- `DeleteCategoryUseCase`（削除時 Stock.categoryId=null）
- `ReorderCategoriesUseCase`

### UI / ViewModel
- `CategoryEditViewModel`
    - 追加 / 編集 / 削除 / 並び替え
    - ボトムシート管理
    - 空リストメッセージ

### UI / Screen
- `CategoryEditScreen`
    - 並び替え可能リスト
    - FAB → 追加ボトムシート
    - 編集アイコン → 編集ボトムシート

---

## スレッド3（⑤保管場所編集画面）

### Domain
- `GetLocationsUseCase`
- `UpsertLocationUseCase`
- `DeleteLocationUseCase`（削除時 Stock.locationId=null）
- `ReorderLocationsUseCase`

### UI / ViewModel
- `LocationEditViewModel`
    - アイコン選択
    - 追加 / 編集 / 削除 / 並び替え

### UI / Screen
- `LocationEditScreen`
    - アイコン表示
    - アイコンパレット（6×5）
    - 並び替え対応リスト
    - FAB → 追加ボトムシート

---

## スレッド4（①ストック一覧画面：本実装）

### Domain
- `GetStockListFlowUseCase`
- `UpdateStockQuantityUseCase`
- `DeleteStockUseCase`
- ソートロジック（第一キー＋第二キー：登録日時）

### UI / ViewModel
- `StockListViewModel`
    - 検索条件・ソート状態
    - 数量変更（0〜30）
    - スワイプ削除（数量0のみ）
    - カテゴリ名/場所アイコンは ④⑤ と連動

### UI / Screen
- `StockListScreen`
    - 3つのソートボタン
    - 残り日数表示
    - 数量 + / −
    - スワイプ削除
    - FAB → 追加へ

---

## スレッド5（②追加 & ③編集：共通フォーム）

### Domain
- `CreateOrUpdateStockUseCase`
- `GetStockByIdUseCase`

### UI / 共通フォーム
- `StockEditFormState`
    - name / quantity / dates / categoryId / locationId / errors
    - バリデーション
    - 保存ボタン活性条件
    - 差分検出（変更済み判定）

### UI / 追加
- `AddStockViewModel`
- `AddStockScreen`

### UI / 編集
- `EditStockViewModel`
- `EditStockScreen`（削除ボタンあり）

---

## スレッド6（⑥ About画面 + 最終調整）

### Domain
- `GetAppInfoUseCase`
- ライセンス一覧の取得方法を決定（手書き/JSON/OSS Plugin など）

### UI
- `AboutAppViewModel`
- `AboutAppScreen`
    - アイコン
    - バージョン
    - ライセンス一覧

### 調整
- 全ナビゲーション確認
- ユースケース単体テスト
- UIテスト
- 軽いリファクタリング
