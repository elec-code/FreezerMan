package com.yasumu.core.domain.repository

import com.yasumu.core.domain.model.Location
import com.yasumu.core.domain.model.LocationId
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getAllLocations(): Flow<List<Location>>

    suspend fun getLocationById(id: LocationId): Location?

    suspend fun upsertLocation(location: Location)

    suspend fun deleteLocation(id: LocationId)
}
