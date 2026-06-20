package com.karuhun.feature.restaurant.data.paging

import com.karuhun.core.common.BasePagingSource
import com.karuhun.core.common.orZero
import com.karuhun.core.database.model.FoodCategoryEntity
import com.karuhun.core.database.model.FoodEntity
import com.karuhun.core.network.model.BaseResponse
import com.karuhun.feature.restaurant.data.source.RestaurantApiService
import com.karuhun.feature.restaurant.data.source.remote.response.GetCategoryResponse
import com.karuhun.feature.restaurant.data.source.remote.response.GetFoodsResponse

internal class FoodPagingSource(
    private val restaurantApiService: RestaurantApiService
) : BasePagingSource<FoodEntity, GetFoodsResponse>() {

    override suspend fun fetchData(page: Int, pageSize: Int): List<GetFoodsResponse> {
        val params = mutableMapOf<String, String>().apply {
            put("paginate", pageSize.toString())
            put("page", page.toString())
        }

        val response = restaurantApiService.getFoods(params)
        return response.data?.data ?: emptyList()
    }

    override fun mapToLocalData(remoteData: List<GetFoodsResponse>): List<FoodEntity> {
        return remoteData.map { response ->
            FoodEntity(
                id = response.id.orZero(),
                name = response.name,
                description = response.description,
                image = response.image,
                foodCategoryId = response.foodCategoryId
            )
        }
    }
}
