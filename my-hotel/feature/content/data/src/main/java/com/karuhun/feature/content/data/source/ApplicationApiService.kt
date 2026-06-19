package com.karuhun.feature.content.data.source

import com.karuhun.core.network.model.BasePaginationResponse
import com.karuhun.core.network.model.BaseResponse
import com.karuhun.core.model.NetworkChangeList
import com.karuhun.feature.content.data.source.remote.response.GetApplicationsResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApplicationApiService {
    @GET("applications")
    suspend fun getApplications(
        @QueryMap(encoded = true) params: Map<String, String>
    ) : BaseResponse<BasePaginationResponse<GetApplicationsResponse>>
    @GET("changelist/applications")
    suspend fun getApplicationChangelist(
        @QueryMap(encoded = true) params: Map<String, String>
    ) : BaseResponse<BasePaginationResponse<NetworkChangeList>>
}