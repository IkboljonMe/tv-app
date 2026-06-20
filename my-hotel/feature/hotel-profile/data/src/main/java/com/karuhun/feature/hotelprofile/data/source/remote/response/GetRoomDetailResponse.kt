package com.karuhun.feature.hotelprofile.data.source.remote.response

import com.google.gson.annotations.SerializedName
import com.karuhun.core.model.RoomDetail

data class GetRoomDetailResponse(

	@field:SerializedName("guest_name")
	val guestName: String? = null,

	@field:SerializedName("greeting")
	val greeting: String? = null,

	@field:SerializedName("is_birthday")
	val isBirthday: Int? = null
)

fun GetRoomDetailResponse?.toDomain() = RoomDetail(
    guestName = this?.guestName.orEmpty(),
    greeting = this?.greeting,
    isBirthday = this?.isBirthday?.let { it == 1 }
)