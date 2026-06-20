package com.hotel.kitchenpos.data

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Holds the configured backend URL and persists the auth cookie
 * (`hm_session`) so the kitchen stays logged in across restarts.
 *
 * Initialise once from [com.hotel.kitchenpos.KitchenPosApp].
 */
object AppSession {
    private const val PREFS = "kitchen_pos"
    private const val KEY_BASE_URL = "base_url"
    private const val KEY_COOKIE = "session_cookie"
    private const val KEY_HOTEL = "pos_hotel_id"

    // 10.0.2.2 is the host machine's localhost as seen from an Android emulator;
    // on a real tablet, set this to the backend's LAN address on the login screen.
    const val DEFAULT_BASE_URL = "http://10.0.2.2:3001"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    var baseUrl: String
        get() = prefs.getString(KEY_BASE_URL, DEFAULT_BASE_URL)!!.trimEnd('/')
        set(value) {
            prefs.edit().putString(KEY_BASE_URL, value.trim().trimEnd('/')).apply()
        }

    /** Raw `name=value` of the persisted session cookie, or null when logged out. */
    private var storedCookie: String?
        get() = prefs.getString(KEY_COOKIE, null)
        set(value) {
            prefs.edit().apply {
                if (value == null) remove(KEY_COOKIE) else putString(KEY_COOKIE, value)
            }.apply()
        }

    /** Last hotel chosen on the board (parallels the web localStorage key). */
    var posHotelId: String?
        get() = prefs.getString(KEY_HOTEL, null)
        set(value) {
            prefs.edit().apply {
                if (value.isNullOrEmpty()) remove(KEY_HOTEL) else putString(KEY_HOTEL, value)
            }.apply()
        }

    val isLoggedIn: Boolean get() = storedCookie != null

    fun logout() {
        storedCookie = null
    }

    /**
     * A cookie jar that captures the backend's `hm_session` cookie and replays
     * it on every request, persisting it across app launches.
     */
    val cookieJar: CookieJar = object : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookies.firstOrNull { it.name == "hm_session" }?.let { cookie ->
                storedCookie = "${cookie.name}=${cookie.value}"
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            val raw = storedCookie ?: return emptyList()
            val name = raw.substringBefore('=')
            val value = raw.substringAfter('=')
            val cookie = Cookie.Builder()
                .name(name)
                .value(value)
                .domain(url.host)
                .path("/")
                .build()
            return listOf(cookie)
        }
    }
}
