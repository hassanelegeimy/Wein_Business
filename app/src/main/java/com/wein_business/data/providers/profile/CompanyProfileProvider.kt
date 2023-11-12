package com.wein_business.data.providers.profile

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.profile.CompanyProfileModel
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class CompanyProfileProvider(var listener: Listener) {
    interface Listener {
        fun onCompanyProfileSuccess(companyProfileModel: CompanyProfileModel)
        fun onCompanyProfileFail(message: String)
    }

    fun getCompanyProfile() {
        //Static data
//        listener.onCompanyProfileSuccess(AssetsProvider.getCompanyProfile())

            val apiCall = RestClient.getApi().getCompanyProfile()
            apiCall.enqueue(AppCallback(object :
                AppCallbackListener<AppResponse<CompanyProfileModel>> {
                override fun onDataSuccess(body: AppResponse<CompanyProfileModel>) {
                    listener.onCompanyProfileSuccess(body.data)
                }

                override fun onDataFail(Message: String) {
                    listener.onCompanyProfileFail(Message)
                }
            }))
    }
}