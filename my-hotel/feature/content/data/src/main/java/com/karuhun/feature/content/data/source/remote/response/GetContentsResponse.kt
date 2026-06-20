package com.karuhun.feature.content.data.source.remote.response

import com.google.gson.annotations.SerializedName
import com.karuhun.core.common.orZero
import com.karuhun.core.model.Content

data class GetContentsResponse(

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("hotel_id")
	val hotelId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

fun List<GetContentsResponse>.toDomainList(): List<Content> {
    return this.orEmpty().map { response ->
        Content(
            id = response.id.orZero(),
            title = response.name.orEmpty(),
            image = response.image.orEmpty(),
            isActive = response.isActive,
        )
    }
}