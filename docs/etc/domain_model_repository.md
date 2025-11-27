# domainデータモデル
## 1.カテゴリ(Category.kt)

```kotlin
package com.yasumu.core.domain.model

@JvmInline
value class CategoryOrder(val value: Int)

/**
 * 食材カテゴリ（肉・魚・野菜など）
 */
data class Category(
    val id: CategoryId,
    val name: String,
    val order: CategoryOrder,
)
```

## 2.保管場所(.kt)
```kotlin
package com.yasumu.core.domain.model

@JvmInline
value class LocationOrder(val value: Int)

/**
 * 保管場所（冷凍室・冷蔵室・野菜室・サブ冷凍庫など）
 */
data class Location(
    val id: LocationId,
    val name: String,
    val order: LocationOrder,
)
```

## 3.ストック(.kt)
```
package com.yasumu.core.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@JvmInline
value class StockId(val value: Long)

@JvmInline
value class CategoryId(val value: Long)

@JvmInline
value class LocationId(val value: Long)

data class Stock(
    val id: StockId,
    val name: String,
    val quantity: Int,

    val bestBeforeDate: LocalDate,
    val cookedDate: LocalDate,

    val registeredAt: Instant,

    val categoryId: CategoryId?,
    val locationId: LocationId?,
)
```

# リポジトリI/F
```
# Repository Interfaces (Domain Layer)

interface StockRepository {
    fun getAllStocks(): Flow<List<Stock>>
    suspend fun getStockById(id: StockId): Stock?

    // - stock.id が StockId(0) の場合: 新規追加として扱い、保存後に採番された ID を持つ Stock を返す。
    // - それ以外の場合: 既存レコードを更新し、更新後の Stock を返す。
    suspend fun upsertStock(stock: Stock): Stock
    suspend fun deleteStock(id: StockId)
}

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: CategoryId): Category?
    suspend fun upsertCategory(category: Category)
    suspend fun deleteCategory(id: CategoryId)
}

interface LocationRepository {
    fun getAllLocations(): Flow<List<Location>>
    suspend fun getLocationById(id: LocationId): Location?
    suspend fun upsertLocation(location: Location)
    suspend fun deleteLocation(id: LocationId)
}
```
