package com.wein_business

import android.app.Application
import android.content.Context
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.Preferences
import com.wein_business.utils.SessionManager

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        Preferences.init(appContext)
        SessionManager.init()
    }

    companion object {
        lateinit var appContext: Context
        lateinit var currentActivity: GenericActivity
    }
}