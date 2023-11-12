package com.wein_business.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.wein_business.R
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.SessionManager

class LauncherActivity : GenericActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        Handler(Looper.getMainLooper()).postDelayed({
            if (SessionManager.isGuest)
                activityLogin(false)
            else
                activityMain()
            //TODO OPEN ONBOARDING FIRST
            // activityOnBoarding()

        }, 1000)
    }
}