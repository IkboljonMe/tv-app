package com.karuhun.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.karuhun.core.model.Content

@Entity(
    tableName = "content",
)
data class ContentEntity(
    @PrimaryKey
    val id: Int,
    val name: String?,
    val image: String?,
    val isActive: Boolean?,
)

fun ContentEntity.toModel() = Content(
    id = id,
    title = name,
    image = image,
    isActive = isActive,
)

fun Content.toEntity() = ContentEntity(
    id = id ?: 0,
    name = title,
    image = image,
    isActive = isActive,
)

fun List<Content>.toEntity() : List<ContentEntity> {
    return this.map {
        it.toEntity()
    }
}

fun List<ContentEntity>.toModel() : List<Content> {
    return this.map {
        it.toModel()
    }
}