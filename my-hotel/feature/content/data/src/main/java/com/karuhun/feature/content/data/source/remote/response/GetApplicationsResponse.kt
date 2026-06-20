package com.karuhun.feature.content.data.source.remote.response

import com.google.gson.annotations.SerializedName
import com.karuhun.core.common.orZero
import com.karuhun.core.model.Application

data class GetApplicationsResponse(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("hotel_id")
	val hotelId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("package_name")
	val packageName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

fun GetApplicationsResponse?.toDomain() = Application(
    id = this?.id.orZero(),
    name = this?.name.orEmpty(),
    image = this?.image.orEmpty(),
    packageName = this?.packageName.orEmpty()
)

fun List<GetApplicationsResponse>?.toDomainList(): List<Application> {
    return this?.map { it.toDomain() } ?: emptyList()
}