package com.karuhun.feature.restaurant.data.source.remote

import com.karuhun.core.datastore.LauncherPreferencesDatastore
import com.karuhun.core.model.Food
import com.karuhun.core.model.FoodCategory
import com.karuhun.core.model.NetworkChangeList
import com.karuhun.feature.restaurant.data.source.RestaurantApiService
import com.karuhun.feature.restaurant.data.source.remote.response.toDomainList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantNetworkDataSource @Inject constructor(
    private val restaurantApiService: RestaurantApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val preferencesDatastore: LauncherPreferencesDatastore
) {
    suspend fun getFoodCategories(ids: List<Int>): List<FoodCategory> {
        return withContext(ioDispatcher) {
            val allFoods = mutableListOf<FoodCategory>()
            var currentPage = 1

            do {
                val params = mapOf(
                    "order" to "asc",
                    "paginate" to "10",
                    "page" to "$currentPage",
                    "ids" to ids.joinToString(",")
                )
                val response = restaurantApiService.getFoodCategories(params)
                allFoods.addAll(response.data?.data?.toDomainList() ?: emptyList())
                currentPage++
            } while (response.data?.nextPageUrl != null)
            allFoods
        }
    }

    suspend fun getFoodCategoriesChangeList(after: Int) : List<NetworkChangeList> {
        return withContext(ioDispatcher) {
            val allChangeLists = mutableListOf<NetworkChangeList>()
            var currentPage = 1
            do {
                val params = mapOf(
                    "order" to "asc",
                    "orderBy" to "food_categories.version",
                    "paginate" to "10",
                    "page" to "$currentPage",
                    "after" to after.toString()
                )
                val response = restaurantApiService.getFoodCategoryChangeList(params)
                allChangeLists.addAll(response.data?.data ?: emptyList())
                currentPage++
            } while (response.data?.nextPageUrl != null)
            allChangeLists
        }
    }

    suspend fun getFoods(
        ids: List<Int>
    ): List<Food> {
        return  withContext(ioDispatcher) {
            val allFoods = mutableListOf<Food>()
            var currentPage = 1

            do {
                val params = mapOf(
                    "order" to "asc",
                    "paginate" to "10",
                    "page" to "$currentPage",
                    "ids" to ids.joinToString(",")
                )
                val response = restaurantApiService.getFoods(params)
                allFoods.addAll(response.data?.data?.toDomainList() ?: emptyList())
                currentPage++
            } while (response.data?.nextPageUrl != null)
            allFoods
        }
    }

    suspend fun getFoodChangelist(after: Int) : List<NetworkChangeList> {
        return withContext(ioDispatcher) {
            val allChangeLists = mutableListOf<NetworkChangeList>()
            var currentPage = 1
            do {
                val params = mapOf(
                    "order" to "asc",
                    "orderBy" to "food.version",
                    "paginate" to "10",
                    "page" to "$currentPage",
                    "after" to after.toString()
                )
                val response = restaurantApiService.getFoodChangeList(params)
                allChangeLists.addAll(response.data?.data ?: emptyList())
                currentPage++
            } while (response.data?.nextPageUrl != null)
            allChangeLists
        }
    }
}
