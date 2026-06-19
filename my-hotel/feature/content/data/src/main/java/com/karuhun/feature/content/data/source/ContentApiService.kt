package com.karuhun.feature.content.data.source

import com.karuhun.core.network.model.BasePaginationResponse
import com.karuhun.core.network.model.BaseResponse
import com.karuhun.core.model.NetworkChangeList
import com.karuhun.feature.content.data.source.remote.response.GetContentItemsResponse
import com.karuhun.feature.content.data.source.remote.response.GetContentsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ContentApiService {

    @GET("contents")
    suspend fun getContents(
        @QueryMap(encoded = true) params: Map<String, String>
    ): BaseResponse<BasePaginationResponse<GetContentsResponse>>

    @GET("changelist/contents")
    suspend fun getContentChangeList(
        @QueryMap(encoded = true) params: Map<String, String>
    ): BaseResponse<BasePaginationResponse<NetworkChangeList>>

    @GET("content-items")
    suspend fun getContentItems(
        @QueryMap(encoded = true) params: Map<String, String>
    ): BaseResponse<BasePaginationResponse<GetContentItemsResponse>>

    @GET("changelist/content-items")
    suspend fun getContentItemChangeList(
        @QueryMap(encoded = true) params: Map<String, String>
    ): BaseResponse<BasePaginationResponse<NetworkChangeList>>
}