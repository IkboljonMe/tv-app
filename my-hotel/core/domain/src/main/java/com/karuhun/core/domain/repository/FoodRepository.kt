package com.karuhun.core.domain.repository

import com.karuhun.core.common.util.Syncable
import com.karuhun.core.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodRepository : Syncable {
    fun getFoodsByCategoryId(categoryId: Int): Flow<List<Food>>
}