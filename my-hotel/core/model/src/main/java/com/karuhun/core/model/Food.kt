package com.karuhun.core.model

data class Food(
    val id: Int,
    val name: String?,
    val description: String?,
    val price: Int?,
    val imageUrl: String?,
    val categoryId: Int?,
    val isDeleted: Boolean?
)
