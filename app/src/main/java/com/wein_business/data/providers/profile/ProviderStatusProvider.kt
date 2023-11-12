package com.wein_business.data.providers.profile

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.ProviderStatusModel
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class ProviderStatusProvider(var listener: Listener) {
    interface Listener {
        fun onProviderStatusSuccess(providerStatusModel: ProviderStatusModel)
        fun onProviderStatusFail(message: String)
    }

    fun getProviderStatus() {
        val apiCall = RestClient.getApi().getProviderStatus()
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<ProviderStatusModel>> {
            override fun onDataSuccess(body: AppResponse<ProviderStatusModel>) {
                listener.onProviderStatusSuccess(body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onProviderStatusFail(Message)
            }
        }))
    }
}