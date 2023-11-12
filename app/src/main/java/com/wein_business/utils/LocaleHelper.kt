package com.wein_business.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.wein_business.BaseApp
import java.util.*


class LocaleHelper {

    companion object {
        fun onAttach(): Context {
            val lang = "ar"
//                    if(Preferences.getLocale()?.isEmpty() == true) {
//            lang = Resources.getSystem().getConfiguration().locale.getLanguage();
//            Preferences.getSharedInstance().setLocale(lang);
//        }else{
//            lang = Preferences.getSharedInstance().getLocale();
//        }
            return setLocale(BaseApp.appContext, "ar")
        }

        private fun setLocale(context: Context, lang: String?): Context {
            var context = context
            val locale = Locale(lang)
            val res: Resources = context.resources
            val configuration = Configuration(res.configuration)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Locale.setDefault(locale)
                configuration.setLocale(locale)
                configuration.setLayoutDirection(locale)
            } else {
                configuration.setLocale(locale)
                configuration.setLayoutDirection(configuration.locale)
                Locale.setDefault(locale)
            }
            context = context.createConfigurationContext(configuration)
            return context
        }
    }





}