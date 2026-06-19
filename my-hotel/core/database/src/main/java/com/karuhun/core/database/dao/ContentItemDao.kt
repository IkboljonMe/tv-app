package com.karuhun.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.karuhun.core.database.model.ContentItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentItemDao {
    @Upsert
    suspend fun upsert(data: List<ContentItemEntity>)

    @Query("SELECT * FROM content_item WHERE contentId = :contentId")
    fun getByContentId(contentId: Int): Flow<List<ContentItemEntity>>

    @Query("DELETE FROM content_item WHERE id in (:ids)")
    suspend fun deleteById(ids: List<Int>)
}