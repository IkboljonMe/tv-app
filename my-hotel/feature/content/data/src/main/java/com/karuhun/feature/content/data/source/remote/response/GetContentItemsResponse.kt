package com.karuhun.feature.content.data.source.remote.response

import com.google.gson.annotations.SerializedName
import com.karuhun.core.common.orZero
import com.karuhun.core.model.ContentItem

data class GetContentItemsResponse(

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("content_id")
	val contentId: Int? = null
)

fun GetContentItemsResponse.toDomainModel() = ContentItem(
    id = id ?: 0,
    name = name.orEmpty(),
    image = image?.toString().orEmpty(),
    description = description.orEmpty(),
    contentId = contentId.orZero()
)

fun List<GetContentItemsResponse>?.toDomainList() = this.orEmpty().map { it.toDomainModel() }