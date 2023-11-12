package com.wein_business.data.providers.product

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.product.ProviderProductModel
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class ProviderProductDetailsProvider(var listener: Listener) {
    interface Listener {
        fun onProviderProductDetailsSuccess(providerProductModel: ProviderProductModel)
        fun onProviderProductDetailFail(message: String)
    }

    fun getProviderProductDetails(activityId: Int) {
        val apiCall = RestClient.getApi().getProviderProductDetails(activityId)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<ProviderProductModel>> {
            override fun onDataSuccess(body: AppResponse<ProviderProductModel>) {
                listener.onProviderProductDetailsSuccess(body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onProviderProductDetailFail(Message)
            }
        }))
    }
}