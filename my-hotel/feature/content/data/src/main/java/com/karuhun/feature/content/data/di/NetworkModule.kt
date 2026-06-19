package com.karuhun.feature.content.data.di

import com.karuhun.core.common.network.Dispatcher
import com.karuhun.core.common.network.LauncherDispatcher
import com.karuhun.feature.content.data.source.ApplicationApiService
import com.karuhun.feature.content.data.source.ContentApiService
import com.karuhun.feature.content.data.source.remote.ApplicationNetworkDataSource
import com.karuhun.feature.content.data.source.remote.ContentNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideContentApiService(retrofit: Retrofit): ContentApiService =
        retrofit.create(ContentApiService::class.java)

    @Provides
    @Singleton
    fun provideApplicationApiService(retrofit: Retrofit): ApplicationApiService =
        retrofit.create(ApplicationApiService::class.java)

    @Provides
    @Singleton
    fun provideApplicationNetworkDataSource(
        apiService: ApplicationApiService,
        @Dispatcher(LauncherDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ) : ApplicationNetworkDataSource{
        return ApplicationNetworkDataSource(
            apiService = apiService,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideContentNetworkDataSource(
        apiService: ContentApiService,
        @Dispatcher(LauncherDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ) : ContentNetworkDataSource{
        return ContentNetworkDataSource(
            apiService = apiService,
            ioDispatcher = ioDispatcher
        )
    }
}