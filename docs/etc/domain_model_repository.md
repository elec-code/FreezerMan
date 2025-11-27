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
    fun getStocks(): Flow<List<Stock>>
    suspend fun getStock(id: StockId): Stock?
    suspend fun upsert(stock: Stock)
    suspend fun delete(id: StockId)
}

interface CategoryRepository { ... }
interface LocationRepository  { ... }
```
