package com.wein_business.utils

import com.wein_business.data.model.ProviderStatusModel
import com.wein_business.data.model.authorize.UserModel
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.enums.UserType

object SessionManager {

    lateinit var userModel: UserModel
    lateinit var providerStatusModel: ProviderStatusModel

    var isGuest = false
    var isUserIndividual = false
    var isUserCompany = false

    //TODO connect to locale
    var sessionLang = Constants.LOCALE_DEFAULT


    //*********************************************************************
    //****** Session Temp Data
    var isNewProductCreatedUpdated = false
    var isNewTripCreated = false

    fun init() {
        initSessionUserModel(Preferences.getUserModel())
        initProviderStatusModel(Preferences.getProviderStatusModel())
    }

    private fun initSessionUserModel(userModel: UserModel) {
        this.userModel = userModel
        isGuest = SessionManager.userModel.userType == UserType.GUEST.toString()
        isUserIndividual = SessionManager.userModel.userType == UserType.PROVIDER_USER.toString()
        isUserCompany = SessionManager.userModel.userType == UserType.PROVIDER_COMPANY.toString()
    }

    fun changeSessionUserModel(userModel: UserModel) {
        Preferences.setUserModel(userModel)
        initSessionUserModel(userModel)
    }

    //******************* Status

    private fun initProviderStatusModel(providerStatusModel: ProviderStatusModel) {
        this.providerStatusModel = providerStatusModel
    }

    fun changeProviderStatusModel(providerStatusModel: ProviderStatusModel) {
        this.providerStatusModel = providerStatusModel
        Preferences.setProviderStatusModel(providerStatusModel)
    }

    //****************** General

    fun logout(activity: GenericActivity) {
        var defaultStatusModel = Preferences.getDefaultProviderStatusModel()
        changeProviderStatusModel(defaultStatusModel)

        var guestUserModel = Preferences.getGuestUserModel()
        changeSessionUserModel(guestUserModel)
        activity.activityLogin(true)
    }
}