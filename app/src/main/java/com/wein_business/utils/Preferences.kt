package com.wein_business.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.wein_business.BaseApp
import com.wein_business.R
import com.wein_business.data.model.ProviderStatusModel
import com.wein_business.data.model.authorize.UserModel
import com.wein_business.utils.enums.ProviderStatusEnum
import com.wein_business.utils.enums.UserType

object Preferences {

    private lateinit var preferences: SharedPreferences
    private lateinit var preferencesEditor: SharedPreferences.Editor

    private lateinit var userModel: UserModel
    private lateinit var providerStatusModel: ProviderStatusModel

    fun init(context: Context) {
        preferences = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE)
        preferencesEditor = preferences.edit()
    }

    //*************************************************************************
    //*************************************************************************

    fun getString(key: String, value: String): String? {
        return preferences.getString(key, value)
    }

    private fun setString(key: String, value: String) {
        with(preferencesEditor) {
            putString(key, value)
            commit()
        }
    }

    //*************************************************************************
    //*************************************************************************

    fun getUserModel(): UserModel {
        if (::userModel.isInitialized)
            return userModel

        val userModelString: String? = getString(Constants.KEY_USER_MODEL, "")//default

        userModel = if (userModelString == null || userModelString == "")
            getGuestUserModel()
        else
            Gson().fromJson(userModelString, UserModel::class.java)

        return userModel
    }

    fun setUserModel(userModel: UserModel) {
        this.userModel = userModel
        val gson = Gson()
        val userModelJson = gson.toJson(userModel)
        setString(Constants.KEY_USER_MODEL, userModelJson)
    }

    fun getProviderStatusModel(): ProviderStatusModel {
        if (::providerStatusModel.isInitialized)
            return providerStatusModel

        val statusModelString: String? = getString(Constants.KEY_PROVIDER_STATUS_MODEL, "")//default

        providerStatusModel = if (statusModelString == null || statusModelString == "")
            getDefaultProviderStatusModel()
        else
            Gson().fromJson(statusModelString, ProviderStatusModel::class.java)

        return providerStatusModel
    }

    fun setProviderStatusModel(providerStatusModel: ProviderStatusModel) {
        this.providerStatusModel = providerStatusModel
        val gson = Gson()
        val statusModelJson = gson.toJson(providerStatusModel)
        setString(Constants.KEY_PROVIDER_STATUS_MODEL, statusModelJson)
    }

    //*************************************************************************
    //*************************************************************************


    //******************************* Defaults
    fun getGuestUserModel(): UserModel {
        var guestUserModel = UserModel()
        guestUserModel.userType = UserType.GUEST.toString()
        return guestUserModel
    }

    fun getDefaultProviderStatusModel(): ProviderStatusModel {
        var defaultProviderStatusModel = ProviderStatusModel()
        defaultProviderStatusModel.userStatusEnum = ProviderStatusEnum.UNDEFINED.toString()
        defaultProviderStatusModel.userStatusLookup.nameAr = BaseApp.appContext.getString(R.string.status_undefined)
        defaultProviderStatusModel.userStatusLookup.nameEn = BaseApp.appContext.getString(R.string.status_undefined)

        return defaultProviderStatusModel
    }

}