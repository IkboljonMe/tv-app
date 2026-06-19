package com.karuhun.core.network.model

import com.google.gson.annotations.SerializedName

open class BaseResponse<T>(
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("message")
    val message: String? = null,
)