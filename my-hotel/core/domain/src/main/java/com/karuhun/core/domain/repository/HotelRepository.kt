package com.karuhun.core.domain.repository

import com.karuhun.core.common.Resource
import com.karuhun.core.common.util.Syncable
import com.karuhun.core.datastore.HotelProfile
import com.karuhun.core.model.RoomDetail
import kotlinx.coroutines.flow.Flow

interface HotelRepository : Syncable {
    suspend fun getHotelProfile(): Flow<HotelProfile>
    suspend fun getRoomDetail(): Resource<RoomDetail>
}