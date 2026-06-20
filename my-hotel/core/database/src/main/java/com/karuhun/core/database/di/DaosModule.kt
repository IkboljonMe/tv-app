package com.karuhun.core.database.di

import com.karuhun.core.database.LauncherDatabase
import com.karuhun.core.database.dao.ContentDao
import com.karuhun.core.database.dao.HotelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun provideHotelDao(
        database: LauncherDatabase
    ): HotelDao = database.hotelDao()

    @Provides
    fun provideContentDao(
        database: LauncherDatabase
    ): ContentDao = database.contentDao()

    @Provides
    fun provideApplicationDao(
        database: LauncherDatabase
    ) = database.applicationDao()

    @Provides
    fun provideContentItemDao(
        database: LauncherDatabase
    ) = database.contentItemDao()

    @Provides
    fun provideFoodCategoryDao(
        database: LauncherDatabase
    ) = database.foodCategoryDao()

    @Provides
    fun provideFoodDao(
        database: LauncherDatabase
    ) = database.foodDao()
}