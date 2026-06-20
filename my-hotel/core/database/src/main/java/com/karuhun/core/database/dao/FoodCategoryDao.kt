package com.karuhun.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.karuhun.core.database.model.FoodCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodCategoryDao {

    @Upsert
    suspend fun upsert(items: List<FoodCategoryEntity>)

    @Query("SELECT * FROM food_category")
    fun getAll(): Flow<List<FoodCategoryEntity>>

    @Query("DELETE FROM food_category")
    suspend fun deleteAll()

    @Query("DELETE FROM food_category WHERE id in (:ids)")
    suspend fun deleteByIds(ids: List<Int>)
}