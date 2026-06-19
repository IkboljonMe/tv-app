package com.karuhun.core.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.karuhun.core.model.ContentItem

@Entity(
    tableName = "content_item",
)
data class ContentItemEntity(
    @PrimaryKey
    val id: Int,
    val name: String?,
    val image: String?,
    val description: String?,
    val contentId: Int,
)

fun ContentItemEntity.toDomainModel() = ContentItem(
    id = id,
    name = name.orEmpty(),
    image = image.orEmpty(),
    description = description.orEmpty(),
    contentId = contentId
)

fun ContentItem.toEntity() = ContentItemEntity(
    id = id,
    name = name,
    image = image,
    description = description,
    contentId = contentId
)

fun List<ContentItemEntity>.toDomainModel() = map { it.toDomainModel() }

fun List<ContentItem>.toEntity() = map { it.toEntity() }