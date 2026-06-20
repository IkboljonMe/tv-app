package com.karuhun.core.domain.usecase

import com.karuhun.core.domain.repository.FoodRepository
import javax.inject.Inject

class GetFoodsUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    operator fun invoke(categoryId: Int) = repository.getFoodsByCategoryId(categoryId)
}