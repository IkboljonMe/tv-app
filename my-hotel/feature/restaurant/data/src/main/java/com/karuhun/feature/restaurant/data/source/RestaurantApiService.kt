package com.karuhun.feature.restaurant.data.source

import com.karuhun.core.network.model.BasePaginationResponse
import com.karuhun.core.network.model.BaseResponse
import com.karuhun.core.model.NetworkChangeList
import com.karuhun.feature.restaurant.data.source.remote.response.GetCategoryResponse
import com.karuhun.feature.restaurant.data.source.remote.response.GetFoodsResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RestaurantApiService {
    @GET("foods/categories")
    suspend fun getFoodCategories(
        @QueryMap(encoded = true) params: Map<String, String>
    ): BaseResponse<BasePaginationResponse<GetCategoryResponse>>

    @GET("changelist/foods/categories")
    suspend fun getFoodCategoryChangeList(
        @QueryMap(encoded = true) params: Map<String, String>
    ): BaseResponse<BasePaginationResponse<NetworkChangeList>>

    @GET("foods/items")
    suspend fun getFoods(
        @QueryMap(encoded = true) params: Map<String, String>
    ): BaseResponse<BasePaginationResponse<GetFoodsResponse>>

    @GET("changelist/foods/items")
    suspend fun getFoodChangeList(
        @QueryMap(encoded = true) params: Map<String, String>
    ): BaseResponse<BasePaginationResponse<NetworkChangeList>>
}