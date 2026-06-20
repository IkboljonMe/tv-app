package com.karuhun.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.karuhun.core.database.model.ContentEntity
import com.karuhun.core.database.model.ContentItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
    @Upsert
    suspend fun upsert(data: List<ContentEntity>)

    @Upsert
    suspend fun upsertContentItems(data: List<ContentItemEntity>)

    @Query("SELECT * FROM content WHERE id = :id")
    suspend fun getContentById(id: Int): ContentEntity?

    @Query("SELECT * FROM content_item WHERE contentId= :id")
    fun getContentItemsById(id: Int): Flow<List<ContentItemEntity>>

    @Query("SELECT * FROM content")
    fun getAll(): Flow<List<ContentEntity>>

    @Query("DELETE FROM content")
    suspend fun deleteAll()

    @Query("DELETE FROM content WHERE id in (:ids)")
    suspend fun deleteContentByIds(ids: List<Int>)

    @Query("DELETE FROM content_item WHERE id in (:ids)")
    suspend fun deleteContentItemByIds(ids: List<Int>)
}