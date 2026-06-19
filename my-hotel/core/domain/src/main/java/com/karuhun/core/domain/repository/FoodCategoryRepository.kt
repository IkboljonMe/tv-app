package com.karuhun.core.domain.repository

import com.karuhun.core.common.util.Syncable
import com.karuhun.core.model.FoodCategory
import kotlinx.coroutines.flow.Flow

interface FoodCategoryRepository : Syncable {
    fun getRestaurantCategories(): Flow<List<FoodCategory>?>
}