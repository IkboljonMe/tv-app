package com.karuhun.core.common.network.di

import com.karuhun.core.common.network.Dispatcher
import com.karuhun.core.common.network.LauncherDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Provides
    @Dispatcher(LauncherDispatcher.IO)
    fun proviceIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(LauncherDispatcher.Default)
    fun proviceDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}