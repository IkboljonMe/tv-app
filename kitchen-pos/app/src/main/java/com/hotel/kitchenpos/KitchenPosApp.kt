package com.hotel.kitchenpos

import android.app.Application
import com.hotel.kitchenpos.data.AppSession

class KitchenPosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppSession.init(this)
    }
}
