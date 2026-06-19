package com.karuhun.feature.restaurant.data.paging

import com.karuhun.core.common.BasePagingSource
import com.karuhun.core.database.model.FoodCategoryEntity
import com.karuhun.feature.restaurant.data.source.RestaurantApiService
import com.karuhun.feature.restaurant.data.source.remote.response.GetCategoryResponse

internal class FoodCategoryPagingSource(
    private val restaurantApiService: RestaurantApiService
) : BasePagingSource<FoodCategoryEntity, GetCategoryResponse>() {

    override suspend fun fetchData(page: Int, pageSize: Int): List<GetCategoryResponse> {
        val params = mutableMapOf<String, String>().apply {
            put("paginate", pageSize.toString())
            put("page", page.toString())
        }

        val response = restaurantApiService.getFoodCategories(params)
        return response.data?.data ?: emptyList()
    }

    override fun mapToLocalData(remoteData: List<GetCategoryResponse>): List<FoodCategoryEntity> {
        return remoteData.map { response ->
            FoodCategoryEntity(
                id = response.id,
                name = response.name,
                description = response.description,
                image = response.image
            )
        }
    }
}
