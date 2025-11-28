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


## 改訂版 スレッド2 プラン

### 2-A: Category 用 UseCase & ViewModel 設計（設計メイン）

レイヤ: Domain + ViewModel（設計のみ）

やること（コードは最小限の骨格だけ）:

* UseCase 一覧と責務を整理

    * `GetCategoriesUseCase`
    * `UpsertCategoryUseCase`
    * `DeleteCategoryUseCase`（Stock.categoryId を null にする責務もここに書く）
    * `ReorderCategoriesUseCase`
* 各 UseCase のインターフェース案だけ決める（クラス名・メソッドシグネチャ）
* `CategoryEditUiState` の項目を決める

    * カテゴリ一覧
    * ローディングフラグ
    * 空リストメッセージ用の情報
    * エラー/メッセージ用フィールドを入れるかどうか
* ViewModel が持つイベント/アクションの一覧を決める

    * 初期ロード
    * 追加開始/確定
    * 編集開始/確定
    * 削除要求/確定
    * 並び替え完了

このスレッドでは実装はほぼ書かず、「設計とシグネチャ」を固めるのがゴール。

---

### 2-B: UseCase & ViewModel 実装（一覧ロード + 空リスト）

レイヤ: Domain（UseCase 実装） + ViewModel

やること:

* 2-A で決めた UseCase のうち、まずは

    * `GetCategoriesUseCase`
      を実装
* `CategoryEditViewModel` に `GetCategoriesUseCase` を注入
* 初期ロード処理を実装

    * 画面開始時にカテゴリ一覧を取得
    * `UiState` に反映
    * 0 件なら空リストメッセージのフラグを立てる

このスレッドのゴール:

* ViewModel 単体で「カテゴリ一覧を読み込んで状態として持てる」状態にする
* UI はまだ WIP のままで OK

---

### 2-C: Screen & ViewModel（骨組み）

レイヤ: UI(Screen) + ViewModel

やること:

* `CategoryEditRoute` / `CategoryEditScreen` を、`CategoryEditViewModel` に接続

    * `collectAsState()` で `UiState` を購読
* UI の骨組み

    * Scaffold + TopBar（タイトルのみ）
    * `LazyColumn` でカテゴリ一覧を表示（まだ並び替えなし）
    * FAB を置いて、「追加開始」イベントを ViewModel に送るところまで
* ボトムシートはダミーでよい

    * とりあえず「追加用ボトムシートが開く/閉じる」だけ ViewModel の状態と連動させる

ゴール:

* カテゴリ編集画面に遷移すると

    * 一覧が表示される
    * FAB を押すとボトムシート（ダミー）が開く
* 追加・編集・削除の中身はまだ未実装

---

### 2-D: UseCase & ViewModel（追加・編集 Upsert）

レイヤ: Domain（`UpsertCategoryUseCase` 実装） + ViewModel

やること:

* `UpsertCategoryUseCase` を実装

    * 新規 / 更新の判定ロジックは UseCase 側 or Repository 側の責務を整理
* `CategoryEditViewModel` に

    * 追加モード / 編集モードのモード管理
    * 入力中のカテゴリ名を保持
    * 「決定」イベントで `UpsertCategoryUseCase` を呼ぶ処理
    * 成功時の再ロード + ボトムシートクローズ

ゴール:

* ViewModel だけ見れば、「カテゴリ名を渡して Upsert できる」状態になっている

---

### 2-E: Screen & ViewModel（追加・編集 UI）

レイヤ: UI(Screen) + ViewModel

やること:

* ボトムシートに実際の入力 UI を実装

    * TextField
    * キャンセル/保存ボタン
* 追加用 FAB / 編集アイコン → 「どのカテゴリを編集するか」を ViewModel に渡す
* バリデーションエラー（空文字など）の簡易ハンドリング

ゴール:

* UI 操作だけで「カテゴリ追加」「カテゴリ名変更」が最後まで完結する

---

### 2-F: UseCase & ViewModel（削除 + Stock.categoryId = null）

レイヤ: Domain（`DeleteCategoryUseCase`） + ViewModel

やること:

* `DeleteCategoryUseCase` の実装

    * `CategoryRepository` / `StockRepository` のどちらで何をするかを整理
    * 「指定カテゴリに紐づく Stock の `categoryId` を null にする」責務を明記
* ViewModel:

    * 削除要求イベント
    * 確定イベントで UseCase 呼び出し
    * 成功時に一覧を再ロード

ゴール:

* ViewModel の観点で「削除フロー」が一通りつながっている状態

---

### 2-G: Screen & ViewModel（削除 UI + 確認ダイアログ）

レイヤ: UI(Screen) + ViewModel

やること:

* 各行に「削除」操作（アイコン or スワイプ）を追加
* 削除確認ダイアログを表示

    * OK → ViewModel に「削除確定」イベントを送る
* 完了後にスナックバーなどでフィードバック（余裕があれば）

ゴール:

* UI からカテゴリ削除ができ、連動して Stock の categoryId も null になる（仕様上そうなっている）

---

### 2-H: UseCase & ViewModel（並び替え）

レイヤ: Domain（`ReorderCategoriesUseCase`） + ViewModel

やること:

* `ReorderCategoriesUseCase` のインターフェースと実装

    * 例: `invoke(newOrder: List<CategoryId>)`
* ViewModel:

    * 並び替えイベント（fromIndex / toIndex）を受けて、UiState のリストを並び替え
    * ドラッグ終了時などに UseCase を呼んで永続化

ゴール:

* ViewModel のレベルで並び替え → 永続化が完結している

---

### 2-I: Screen & ViewModel（並び替え UI）

レイヤ: UI(Screen) + ViewModel

やること:

* 並び替え可能なリスト UI を実装

    * ライブラリを使うか、Compose でドラッグ処理を書くかをこのスレッドで決定
* ドラッグ操作 → ViewModel の並び替えイベント呼び出し
* 再描画時に新しい順序が反映されることを確認

ゴール:

* 画面上でカテゴリの順番を変更でき、アプリ再起動後も順番が保たれる


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
