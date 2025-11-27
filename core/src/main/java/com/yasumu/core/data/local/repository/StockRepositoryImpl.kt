package com.yasumu.core.data.local.repository

import com.yasumu.core.data.local.dao.StockDao
import com.yasumu.core.data.local.entity.StockEntity
import com.yasumu.core.domain.model.CategoryId
import com.yasumu.core.domain.model.LocationId
import com.yasumu.core.domain.model.Stock
import com.yasumu.core.domain.model.StockId
import com.yasumu.core.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room をバックエンドとした StockRepository の実装。
 *
 * - stock.id.value == 0L の場合は新規 INSERT として扱い、
 *   DB の autoGenerate で採番された ID を持つ Stock を返す。
 * - それ以外の場合は UPDATE として扱い、引数の Stock をそのまま返す。
 */
class StockRepositoryImpl(
    private val stockDao: StockDao,
) : StockRepository {

    override fun getAllStocks(): Flow<List<Stock>> {
        return stockDao.getAllStocks()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getStockById(id: StockId): Stock? {
        return stockDao.getStockById(id.value)
            ?.toDomain()
    }

    override suspend fun upsertStock(stock: Stock): Stock {
        return if (stock.id.value == 0L) {
            // 新規追加: ID=0 で insert し、Room に ID 採番を任せる
            val entityForInsert = stock.toEntity().copy(id = 0L)
            val newId: Long = stockDao.insert(entityForInsert)
            stock.copy(id = StockId(newId))
        } else {
            // 更新: 既存 ID を持つレコードを上書き
            stockDao.update(stock.toEntity())
            stock
        }
    }

    override suspend fun deleteStock(id: StockId) {
        stockDao.deleteById(id.value)
    }
}

/*
 * ===== Mapper =====
 * Domain <-> Entity の相互変換
 */

// Entity -> Domain
private fun StockEntity.toDomain(): Stock {
    return Stock(
        id = StockId(id),
        name = name,
        quantity = quantity,
        bestBeforeDate = bestBeforeDate,
        cookedDate = cookedDate,
        registeredAt = registeredAt,
        categoryId = categoryId?.let(::CategoryId),
        locationId = locationId?.let(::LocationId),
    )
}

// Domain -> Entity
private fun Stock.toEntity(): StockEntity {
    return StockEntity(
        id = id.value,
        name = name,
        quantity = quantity,
        bestBeforeDate = bestBeforeDate,
        cookedDate = cookedDate,
        registeredAt = registeredAt,
        categoryId = categoryId?.value,
        locationId = locationId?.value,
    )
}
