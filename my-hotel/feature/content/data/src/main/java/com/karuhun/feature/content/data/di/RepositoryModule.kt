package com.karuhun.feature.content.data.di

import com.karuhun.core.domain.repository.ApplicationRepository
import com.karuhun.core.domain.repository.ContentItemsRepository
import com.karuhun.core.domain.repository.ContentRepository
import com.karuhun.feature.content.data.repository.ApplicationRepositoryImpl
import com.karuhun.feature.content.data.repository.ContentItemsRepositoryImpl
import com.karuhun.feature.content.data.repository.ContentRepositoryImpl
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
    abstract fun bindContentRepository(
        impl: ContentRepositoryImpl,
    ): ContentRepository

    @Binds
    @Singleton
    abstract fun bindContentItemsRepository(
        impl: ContentItemsRepositoryImpl
    ): ContentItemsRepository

    @Binds
    @Singleton
    abstract fun bindApplicationRepository(
        impl: ApplicationRepositoryImpl
    ): ApplicationRepository
}