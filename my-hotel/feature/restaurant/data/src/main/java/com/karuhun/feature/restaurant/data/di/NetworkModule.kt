package com.karuhun.feature.restaurant.data.di

import com.karuhun.core.common.network.Dispatcher
import com.karuhun.core.common.network.LauncherDispatcher
import com.karuhun.core.datastore.LauncherPreferencesDatastore
import com.karuhun.feature.restaurant.data.source.RestaurantApiService
import com.karuhun.feature.restaurant.data.source.remote.RestaurantNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    @Singleton
    fun provideRestaurantApiService(
        retrofit: Retrofit
    ): RestaurantApiService = retrofit.create(RestaurantApiService::class.java)

    @Provides
    @Singleton
    fun provideRestaurantNetworkDataSource(
        restaurantApiService: RestaurantApiService,
        @Dispatcher(LauncherDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        preferencesDatastore: LauncherPreferencesDatastore
    ): RestaurantNetworkDataSource {
        return RestaurantNetworkDataSource(
            restaurantApiService = restaurantApiService,
            ioDispatcher = ioDispatcher,
            preferencesDatastore
        )
    }
}