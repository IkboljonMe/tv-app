package com.hotel.kitchenpos.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/** Formatting helpers ported from hotel-menu's `lib/utils.ts`. */
object Format {

    /** Prices are integer UZS, grouped with spaces: "150 000 so'm". */
    fun price(uzs: Long): String {
        val grouped = uzs.toString()
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()
        return "$grouped so'm"
    }

    /** "08:45 AM"-style clock for the createdAt timestamp. */
    fun time(iso: String): String {
        val date = parseIso(iso) ?: return ""
        return clockFmt.format(date)
    }

    /** Whole minutes elapsed since [iso] (never negative). */
    fun minutesAgo(iso: String): Long {
        val date = parseIso(iso) ?: return 0
        val diff = (System.currentTimeMillis() - date.time) / 60_000
        return if (diff < 0) 0 else diff
    }

    private val clockFmt = SimpleDateFormat("hh:mm a", Locale.US)

    private val isoParsers = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
    ).map { pattern ->
        SimpleDateFormat(pattern, Locale.US).apply {
            if (pattern.endsWith("'Z'")) timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    private fun parseIso(iso: String): Date? {
        for (parser in isoParsers) {
            runCatching { return parser.parse(iso) }
        }
        return null
    }
}
