package com.karuhun.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.karuhun.core.model.FoodCategory

@Entity(
    tableName = "food_category",
)
data class FoodCategoryEntity(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val description: String?,
    val image: String?
)

fun FoodCategoryEntity?.toDomain(): FoodCategory {
    return FoodCategory(
        id = this?.id,
        name = this?.name,
        description = this?.description,
        image = this?.image
    )
}

fun List<FoodCategoryEntity>?.toDomainList() : List<FoodCategory>? {
    return this?.map { it.toDomain() }
}

fun FoodCategory.toEntity(): FoodCategoryEntity =
    FoodCategoryEntity(
        id = id,
        name = name,
        description = description,
        image = image
    )

fun List<FoodCategory>.toEntityList(): List<FoodCategoryEntity> {
    return this.map { it.toEntity() }
}