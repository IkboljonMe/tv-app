package com.karuhun.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.karuhun.core.model.Food

@Entity(
    tableName = "foods",
)
data class FoodEntity(
    @PrimaryKey
    val id: Int,
	val image: String? = null,
	val updatedAt: String? = null,
	val foodCategoryId: Int? = null,
	val price: Int? = null,
	val hotelId: Int? = null,
	val name: String? = null,
	val description: String? = null,
	val createdAt: String? = null,
)

fun FoodEntity.toDomain(): Food {
    return Food(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = image,
        categoryId = foodCategoryId,
        isDeleted = false
    )
}

fun List<FoodEntity>.toDomainList(): List<Food> {
    return this.map { it.toDomain() }
}

fun Food.toDomain(): FoodEntity =
    FoodEntity(
        id = id,
        name = name,
        image = imageUrl,
        price = price,
        foodCategoryId = categoryId,
        description = description
    )

fun List<Food>.toEntityList(): List<FoodEntity> {
    return this.map { it.toDomain() }
}