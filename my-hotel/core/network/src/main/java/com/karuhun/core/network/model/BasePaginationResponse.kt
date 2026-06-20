package com.karuhun.core.network.model

import com.google.gson.annotations.SerializedName

open class BasePaginationResponse<T> {
    val data: List<T>? = null
    @SerializedName("next_page_url")
    val nextPageUrl: String? = null
}