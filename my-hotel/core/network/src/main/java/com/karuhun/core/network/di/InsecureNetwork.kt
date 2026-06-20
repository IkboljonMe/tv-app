package com.karuhun.core.network.di

import android.annotation.SuppressLint
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * This file contains components to allow insecure network connections for debugging purposes.
 * DO NOT USE THIS IN PRODUCTION BUILDS.
 */

@SuppressLint("TrustAllX509TrustManager")
fun createInsecureTrustManager(): X509TrustManager {
    return object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }
}

fun createInsecureSslSocketFactory(trustManager: X509TrustManager): SSLSocketFactory {
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, arrayOf(trustManager), java.security.SecureRandom())
    return sslContext.socketFactory
}
