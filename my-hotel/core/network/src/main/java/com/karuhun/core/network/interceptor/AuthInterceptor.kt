package com.karuhun.core.network.interceptor

import com.karuhun.core.network.BuildConfig
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = runBlocking { BuildConfig.TOKEN }

        val requestBuilder = originalRequest.newBuilder()
        token.let {
            requestBuilder.addHeader("X-API-KEY", it)
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}