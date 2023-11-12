package com.wein_business.utils

import android.app.Activity
import android.os.Build
import android.annotation.TargetApi
import android.content.Context
import java.util.*

object LocaleHelper2 {
    fun onAttach(activity: Activity): Context {
        val lang = "ar"
        //    if(Preferences.getLocale().isEmpty()) {
//            lang = Resources.getSystem().getConfiguration().locale.getLanguage();
//            Preferences.getSharedInstance().setLocale(lang);
//        }else{
//            lang = Preferences.getSharedInstance().getLocale();
//        }
        return setLocale(activity, "ar")
    }

    private fun setLocale(context: Context, lang: String): Context {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) updateResources(
            context, lang
        ) else updateResourcesLagacy(
            context, lang
        )
    }

    private fun updateResourcesLagacy(context: Context, lang: String): Context {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) config.setLayoutDirection(
            locale
        )
        resources.updateConfiguration(config, resources.displayMetrics)
        return context
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, lang: String): Context {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(lang))
        res.updateConfiguration(conf, dm)
        return context.createConfigurationContext(config)
    }
}