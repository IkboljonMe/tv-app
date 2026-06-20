package com.karuhun.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.karuhun.core.database.dao.ApplicationDao
import com.karuhun.core.database.dao.ContentDao
import com.karuhun.core.database.dao.ContentItemDao
import com.karuhun.core.database.dao.HotelDao
import com.karuhun.core.database.dao.FoodCategoryDao
import com.karuhun.core.database.dao.FoodDao
import com.karuhun.core.database.model.ApplicationEntity
import com.karuhun.core.database.model.ContentEntity
import com.karuhun.core.database.model.ContentItemEntity
import com.karuhun.core.database.model.HotelEntity
import com.karuhun.core.database.model.FoodCategoryEntity
import com.karuhun.core.database.model.FoodEntity

@Database(
    entities = [
        HotelEntity::class,
        ContentEntity::class,
        ContentItemEntity::class,
        ApplicationEntity::class,
        FoodCategoryEntity::class,
        FoodEntity::class
    ],
    version = 1
)
abstract class LauncherDatabase : RoomDatabase() {
    abstract fun hotelDao() : HotelDao
    abstract fun contentDao(): ContentDao
    abstract fun applicationDao(): ApplicationDao
    abstract fun contentItemDao(): ContentItemDao
    abstract fun foodCategoryDao(): FoodCategoryDao
    abstract fun foodDao(): FoodDao
}