package com.wein_business.data.providers.trip

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.request.AddTripRequest
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.response.NoObjectResponse
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class AddTripProvider(var listener: Listener) {
    interface Listener {
        fun onAddTripSuccess(message: String)
        fun onAddTripFail(message: String)
    }

    fun addTrip(request: AddTripRequest) {
        val apiCall = RestClient.getApi().createTrip(request)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<NoObjectResponse>> {
            override fun onDataSuccess(body: AppResponse<NoObjectResponse>) {
                listener.onAddTripSuccess(body.message)//body.data
            }

            override fun onDataFail(Message: String) {
                listener.onAddTripFail(Message)
            }
        }))
    }
}