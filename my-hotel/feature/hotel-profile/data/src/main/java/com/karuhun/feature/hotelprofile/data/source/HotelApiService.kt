package com.karuhun.feature.hotelprofile.data.source

import com.karuhun.core.network.model.BaseResponse
import com.karuhun.feature.hotelprofile.data.source.remote.response.GetHotelProfileResponse
import com.karuhun.feature.hotelprofile.data.source.remote.response.GetRoomDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HotelApiService {
    @GET("tenant")
    suspend fun getHotelProfile(): BaseResponse<GetHotelProfileResponse>

    @GET("rooms/items/{id}")
    suspend fun getRoomDetail(
        @Path("id") id: String
    ): BaseResponse<GetRoomDetailResponse>
}