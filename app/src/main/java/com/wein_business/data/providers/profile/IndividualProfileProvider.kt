package com.wein_business.data.providers.profile

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.profile.IndividualProfileModel
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class IndividualProfileProvider(var listener: Listener) {
    interface Listener {
        fun onIndividualProfileSuccess(individualProfileModel: IndividualProfileModel)
        fun onIndividualProfileFail(message: String)
    }

    fun getIndividualProfile() {
            val apiCall = RestClient.getApi().getIndividualProfile()
            apiCall.enqueue(AppCallback(object :
                AppCallbackListener<AppResponse<IndividualProfileModel>> {
                override fun onDataSuccess(body: AppResponse<IndividualProfileModel>) {
                    listener.onIndividualProfileSuccess(body.data)
                }

                override fun onDataFail(Message: String) {
                    listener.onIndividualProfileFail(Message)
                }
            }))
    }
}