package com.karuhun.feature.restaurant.data.source.remote.response

import com.google.gson.annotations.SerializedName
import com.karuhun.core.model.Food

data class GetFoodsResponse(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("food_category_id")
	val foodCategoryId: Int? = null,

    @field:SerializedName("is_deleted")
    val isDeleted: Boolean? = null,

	@field:SerializedName("price")
	val price: Int? = null,

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

fun GetFoodsResponse.toDomain() = Food(
    id = id?: 0,
    name = name.orEmpty(),
    description = description.orEmpty(),
    price = price ?: 0,
    imageUrl = image.orEmpty(),
    categoryId = foodCategoryId,
    isDeleted = isDeleted
)

fun List<GetFoodsResponse>.toDomainList(): List<Food> {
    return map { it.toDomain() }
}
