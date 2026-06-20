package com.karuhun.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.karuhun.core.database.model.ApplicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {

    @Query("SELECT * FROM application")
    fun getAll(): Flow<List<ApplicationEntity>>
    @Upsert
    suspend fun upsert(data: List<ApplicationEntity>)

     @Query("SELECT * FROM application WHERE id = :id")
     suspend fun getApplicationById(id: Int): ApplicationEntity?

     @Query("DELETE FROM application")
     suspend fun deleteAll()

     @Query("DELETE FROM application WHERE id in (:ids)")
     suspend fun deleteByIds(ids: List<Int>)
}