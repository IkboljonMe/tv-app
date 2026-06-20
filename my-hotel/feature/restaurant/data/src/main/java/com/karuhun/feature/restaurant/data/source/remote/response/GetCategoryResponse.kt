package com.karuhun.feature.restaurant.data.source.remote.response

import com.google.gson.annotations.SerializedName
import com.karuhun.core.model.FoodCategory

data class GetCategoryResponse(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("hotel_id")
	val hotelId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

fun GetCategoryResponse.toDomain(): FoodCategory =
    FoodCategory(
        id = id,
        name = name,
        description = description,
        image = image
    )

fun List<GetCategoryResponse>.toDomainList(): List<FoodCategory> {
    return this.map { it.toDomain() }
}