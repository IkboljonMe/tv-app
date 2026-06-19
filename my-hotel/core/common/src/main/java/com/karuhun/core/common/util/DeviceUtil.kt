package com.karuhun.core.common.util

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.core.os.EnvironmentCompat

object DeviceUtil {
    fun getDeviceName(context: Context): String {
        return try {
            val deviceName = Settings.Global.getString(context.contentResolver, "device_name")
            when {
                deviceName != null -> deviceName
                Build.VERSION.SDK_INT > 31 -> EnvironmentCompat.MEDIA_UNKNOWN
                else -> Settings.Secure.getString(context.contentResolver, "bluetooth_name") ?: EnvironmentCompat.MEDIA_UNKNOWN
            }
        } catch (e: Exception) {
            EnvironmentCompat.MEDIA_UNKNOWN
        }
    }
}