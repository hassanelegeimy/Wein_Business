package com.wein_business.data.providers.profile

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.profile.CompanyProfileModel
import com.wein_business.data.model.request.UpdateProfileCompanyRequest
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class CompanyProfileUpdateProvider(var listener: Listener) {
    interface Listener {
        fun onCompanyUpdateProfileSuccess(message: String , companyProfileModel: CompanyProfileModel)
        fun onCompanyUpdateProfileFail(message: String)
    }

    fun updateCompanyProfile(
        request: UpdateProfileCompanyRequest
    ) {
        val apiCall = RestClient.getApi().updateCompanyProfile(request)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<CompanyProfileModel>> {
            override fun onDataSuccess(body: AppResponse<CompanyProfileModel>) {
                listener.onCompanyUpdateProfileSuccess(body.message , body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onCompanyUpdateProfileFail(Message)
            }
        }))
    }

    fun submitCompanyProfile(//Same response as update
        request: UpdateProfileCompanyRequest
    ) {
        val apiCall = RestClient.getApi().submitCompanyProfile(request)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<CompanyProfileModel>> {
            override fun onDataSuccess(body: AppResponse<CompanyProfileModel>) {
                listener.onCompanyUpdateProfileSuccess(body.message , body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onCompanyUpdateProfileFail(Message)
            }
        }))
    }
}