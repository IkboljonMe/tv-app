package com.karuhun.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.karuhun.core.database.model.HotelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelDao {
    @Query("SELECT * FROM hotel where id = 1")
    fun getHotelProfile(): Flow<HotelEntity?>

    @Upsert
    suspend fun upsert(data: HotelEntity)

    @Query("DELETE FROM hotel")
    suspend fun deleteAll()
}