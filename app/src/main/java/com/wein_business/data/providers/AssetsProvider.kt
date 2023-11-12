package com.wein_business.data.providers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wein_business.BaseApp
import com.wein_business.data.model.*

object AssetsProvider {

    private fun loadStringFromAsset(fileName : String): String {
        val stringText = BaseApp.appContext.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
        return stringText
    }

    //*********************************************************************************
    //******** Lookups ****************************************************************

    fun getCitiesList(): ArrayList<LookupModel> {
        val json: String = loadStringFromAsset("City.json")
        if (json != null && json.isNotEmpty()) {
            val gson = Gson()
            val lookupType = object : TypeToken<ArrayList<LookupModel>>() {}.type
            return gson.fromJson<ArrayList<LookupModel>>(json, lookupType)
        }
        return ArrayList<LookupModel>()
    }

    fun getCategoriesList(): ArrayList<LookupModel> {
        val json: String = loadStringFromAsset("Category.json")
        if (json != null && json.isNotEmpty()) {
            val gson = Gson()
            val lookupType = object : TypeToken<ArrayList<LookupModel>>() {}.type
            return gson.fromJson<ArrayList<LookupModel>>(json, lookupType)
        }
        return ArrayList<LookupModel>()
    }
}