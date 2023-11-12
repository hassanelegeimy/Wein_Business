package com.wein_business.data.providers.authorize

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.request.authorize.RegisterRequest
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.authorize.OtpModel
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class RegisterProvider(var listener: Listener) {
    interface Listener {
        fun onRegisterSuccess(otpModel : OtpModel)
        fun onRegisterFail(message: String)
    }

    fun register(registerRequest: RegisterRequest) {
        val apiCall = RestClient.getApi().register(registerRequest)

        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<OtpModel>> {
            override fun onDataSuccess(body: AppResponse<OtpModel>) {
                listener.onRegisterSuccess(body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onRegisterFail(Message)
            }
        }))
    }
}