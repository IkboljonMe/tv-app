package com.karuhun.feature.hotelprofile.data.di

import com.karuhun.core.domain.repository.HotelRepository
import com.karuhun.feature.hotelprofile.data.repository.HotelRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindHotelRepository(
        hotelRepositoryImpl: HotelRepositoryImpl
    ) : HotelRepository
}