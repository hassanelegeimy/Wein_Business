package com.wein_business.data.providers.product

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.product.ProviderProductModel
import com.wein_business.data.model.response.AppResponseList
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class ProviderProductsProvider(var listener: Listener) {
    interface Listener {
        fun onProvidersProductsSuccess(productsList: ArrayList<ProviderProductModel>)
        fun onProvidersProductsFail(errorMessage: String?)
    }

    fun getProviderProducts() {
        val apiCall = RestClient.getApi().getProviderProducts()
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponseList<ProviderProductModel>> {
            override fun onDataSuccess(body: AppResponseList<ProviderProductModel>) {
                listener.onProvidersProductsSuccess(body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onProvidersProductsFail(Message)
            }
        }))
    }
}