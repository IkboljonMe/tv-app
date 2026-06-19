package com.karuhun.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.karuhun.core.common.network.Dispatcher
import com.karuhun.core.common.network.LauncherDispatcher
import com.karuhun.core.common.network.di.ApplicationScope
import com.karuhun.core.datastore.Hotel
import com.karuhun.core.datastore.HotelProfile
import com.karuhun.core.datastore.HotelProfileSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton
import com.karuhun.core.datastore.Version
import com.karuhun.core.datastore.VersionSerializer

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideVersionPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(LauncherDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        versionSerializer: VersionSerializer
    ): DataStore<Version> =
        DataStoreFactory.create(
            serializer = versionSerializer,
            scope = scope,
        ) {
            context.dataStoreFile("version.pb")
        }

    @Provides
    @Singleton
    fun provideHotelProfileDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(LauncherDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        hotelProfileSerializer: HotelProfileSerializer
    ): DataStore<Hotel> =
        DataStoreFactory.create(
            serializer = hotelProfileSerializer,
            scope = scope,
        ) {
            context.dataStoreFile("hotel_profile.pb")
        }
}