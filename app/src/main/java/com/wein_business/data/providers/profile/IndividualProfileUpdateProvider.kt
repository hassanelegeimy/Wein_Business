package com.wein_business.data.providers.profile

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.profile.IndividualProfileModel
import com.wein_business.data.model.request.UpdateProfileIndividualRequest
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class IndividualProfileUpdateProvider(var listener: Listener) {
    interface Listener {
        fun onIndividualUpdateProfileSuccess(message: String ,individualProfileModel: IndividualProfileModel)
        fun onIndividualUpdateProfileFail(message: String)
    }

    fun updateIndividualProfile(
        request: UpdateProfileIndividualRequest
    ) {
        val apiCall = RestClient.getApi().updateIndividualProfile(request)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<IndividualProfileModel>> {
            override fun onDataSuccess(body: AppResponse<IndividualProfileModel>) {
                listener.onIndividualUpdateProfileSuccess(body.message , body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onIndividualUpdateProfileFail(Message)
            }
        }))
    }

    fun submitIndividualProfile(//Same response as update
        request: UpdateProfileIndividualRequest
    ) {
        val apiCall = RestClient.getApi().submitIndividualProfile(request)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<IndividualProfileModel>> {
            override fun onDataSuccess(body: AppResponse<IndividualProfileModel>) {
                listener.onIndividualUpdateProfileSuccess(body.message , body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onIndividualUpdateProfileFail(Message)
            }
        }))
    }
}