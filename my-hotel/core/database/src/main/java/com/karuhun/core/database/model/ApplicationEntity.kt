package com.karuhun.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.karuhun.core.model.Application

@Entity(tableName = "application")
data class ApplicationEntity(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val image: String?,
    val packageName: String?,
)

fun ApplicationEntity.toDomain() = com.karuhun.core.model.Application(
    id = this.id ?: 0,
    name = this.name.orEmpty(),
    image = this.image.orEmpty(),
    packageName = this.packageName.orEmpty()
)

fun List<ApplicationEntity>.toDomainList(): List<com.karuhun.core.model.Application> {
    return this.map { it.toDomain() }
}

fun Application.toEntity() = ApplicationEntity(
    id = this.id ?: 0,
    name = this.name.orEmpty(),
    image = this.image.orEmpty(),
    packageName = this.packageName.orEmpty()
)

fun List<Application>.toEntityList(): List<ApplicationEntity> {
    return this.map { it.toEntity() }
}