package com.wein_business.data.providers.product

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.product.ProviderProductModel
import com.wein_business.data.model.request.CreateUpdateProductRequest
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class ProviderProductCreateUpdateProvider(var listener: Listener) {
    interface Listener {
        fun onCreateUpdateProductSuccess(message: String , providerProductModel: ProviderProductModel)
        fun onCreateUpdateProductFail(message: String)
    }

    fun createUpdateProduct(request: CreateUpdateProductRequest) {
        val apiCall = RestClient.getApi().createUpdateProduct(request)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<ProviderProductModel>> {
            override fun onDataSuccess(body: AppResponse<ProviderProductModel>) {
                listener.onCreateUpdateProductSuccess(body.message , body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onCreateUpdateProductFail(Message)
            }
        }))
    }

    fun submitProduct(request: CreateUpdateProductRequest) {//Same response as update
        val apiCall = RestClient.getApi().submitProduct(request)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<ProviderProductModel>> {
            override fun onDataSuccess(body: AppResponse<ProviderProductModel>) {
                listener.onCreateUpdateProductSuccess(body.message , body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onCreateUpdateProductFail(Message)
            }
        }))
    }
}