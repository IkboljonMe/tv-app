package com.karuhun.core.model

data class Hotel(
    val id: Int? = 1,
    val name: String?,
    val phone: String?,
    val email: String?,
    val website: String?,
    val defaultGreeting: String?,
    val passwordSetting: String?,
    val logoWhite: String?,
    val logoBlack: String?,
    val primaryColor: String?,
    val mainPhoto: String?,
    val backgroundPhoto: String?,
    val introVideo: String?,
    val welcomeText: String?,
    val runningText: String?,
) {
    companion object {
        val Empty = Hotel(
            id = null,
            name = null,
            phone = null,
            email = null,
            website = null,
            defaultGreeting = null,
            passwordSetting = null,
            logoWhite = null,
            logoBlack = null,
            primaryColor = null,
            mainPhoto = null,
            backgroundPhoto = null,
            introVideo = null,
            welcomeText = null,
            runningText = null
        )
    }
}