package com.karuhun.feature.restaurant.data.di

import com.karuhun.core.domain.repository.FoodCategoryRepository
import com.karuhun.core.domain.repository.FoodRepository
import com.karuhun.feature.restaurant.data.repository.FoodCategoryRepositoryImpl
import com.karuhun.feature.restaurant.data.repository.FoodRepositoryImpl
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
    abstract fun bindFoodCategoryRepository(impl: FoodCategoryRepositoryImpl): FoodCategoryRepository

    @Binds
    @Singleton
    abstract fun bindFoodRepository(impl: FoodRepositoryImpl): FoodRepository
}