package com.karuhun.core.database.di

import android.content.Context
import androidx.room.Room
import com.karuhun.core.database.LauncherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideLauncherDatabase(
        @ApplicationContext context: Context
    ) : LauncherDatabase = Room.databaseBuilder(
        context,
        LauncherDatabase::class.java,
        "launcher-database"
    ).build()
}