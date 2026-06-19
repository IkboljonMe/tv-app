package com.karuhun.core.domain.usecase

import com.karuhun.core.domain.repository.FoodCategoryRepository
import com.karuhun.core.model.FoodCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoodCategories @Inject constructor(
    private val repository: FoodCategoryRepository
) {
    operator fun invoke(): Flow<List<FoodCategory>?> = repository.getRestaurantCategories()
}