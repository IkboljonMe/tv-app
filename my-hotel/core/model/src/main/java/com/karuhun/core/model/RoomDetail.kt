package com.karuhun.core.model

data class RoomDetail(
    val guestName: String? = null,
    val greeting: String? = null,
    val isBirthday: Boolean? = null,
) {
    companion object {
        val Empty = RoomDetail(
            guestName = "",
            greeting = "",
            isBirthday = false
        )
    }
}
