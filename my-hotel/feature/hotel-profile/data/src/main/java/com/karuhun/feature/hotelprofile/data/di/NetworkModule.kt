package com.karuhun.feature.hotelprofile.data.di

import com.karuhun.feature.hotelprofile.data.source.HotelApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHotelApiService(retrofit: Retrofit) =
        retrofit.create(HotelApiService::class.java)
}