package com.karuhun.launcher

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.karuhun.sync.initializer.Sync
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LauncherApp : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        Sync.initialize(this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }
}