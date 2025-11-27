package com.yasumu.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yasumu.core.data.local.entity.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Query("SELECT * FROM stocks ORDER BY id ASC")
    fun getAllStocks(): Flow<List<StockEntity>>

    @Query("SELECT * FROM stocks WHERE id = :id LIMIT 1")
    suspend fun getStockById(id: Long): StockEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(stock: StockEntity): Long

    @Update
    suspend fun update(stock: StockEntity)

    @Delete
    suspend fun delete(stock: StockEntity)

    @Query("DELETE FROM stocks WHERE id = :id")
    suspend fun deleteById(id: Long)

    // カテゴリ削除時に、そのカテゴリを使っているストックのカテゴリを未設定(null)にする
    @Query("UPDATE stocks SET categoryId = NULL WHERE categoryId = :categoryId")
    suspend fun clearCategory(categoryId: Long)

    // 保管場所削除時に、その保管場所を使っているストックの保管場所を未設定(null)にする
    @Query("UPDATE stocks SET locationId = NULL WHERE locationId = :locationId")
    suspend fun clearLocation(locationId: Long)
}
