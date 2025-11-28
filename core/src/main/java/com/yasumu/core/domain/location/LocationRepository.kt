package com.yasumu.core.domain.location

import com.yasumu.core.domain.stock.LocationId
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getAllLocations(): Flow<List<Location>>

    suspend fun getLocationById(id: LocationId): Location?

    suspend fun upsertLocation(location: Location)

    suspend fun deleteLocation(id: LocationId)
}