package com.yasumu.core.data.local.repository

import com.yasumu.core.data.local.dao.LocationDao
import com.yasumu.core.data.local.entity.LocationEntity
import com.yasumu.core.domain.location.Location
import com.yasumu.core.domain.stock.LocationId
import com.yasumu.core.domain.location.LocationOrder
import com.yasumu.core.domain.location.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room をバックエンドとした LocationRepository の実装。
 *
 * - location.id.value == 0L の場合は新規 INSERT として扱い、
 *   DB の autoGenerate で採番された ID を使う。
 * - それ以外の場合は UPDATE として扱う。
 */
class LocationRepositoryImpl(
    private val locationDao: LocationDao,
) : LocationRepository {

    override fun getAllLocations(): Flow<List<Location>> {
        return locationDao.getAllLocations()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getLocationById(id: LocationId): Location? {
        return locationDao.getLocationById(id.value)
            ?.toDomain()
    }

    override suspend fun upsertLocation(location: Location) {
        if (location.id.value == 0L) {
            // 新規追加: ID=0 で insert し、Room に ID 採番を任せる
            val entityForInsert = location.toEntity().copy(id = 0L)
            locationDao.insert(entityForInsert)
        } else {
            // 更新: 既存 ID を持つレコードを上書き
            locationDao.update(location.toEntity())
        }
    }

    override suspend fun deleteLocation(id: LocationId) {
        locationDao.deleteById(id.value)
    }
}

/*
 * ===== Mapper =====
 * Domain <-> Entity の相互変換
 */

// Entity -> Domain
private fun LocationEntity.toDomain(): Location {
    return Location(
        id = LocationId(id),
        name = name,
        order = LocationOrder(sortOrder),
    )
}

// Domain -> Entity
private fun Location.toEntity(): LocationEntity {
    return LocationEntity(
        id = id.value,
        name = name,
        sortOrder = order.value,
    )
}
