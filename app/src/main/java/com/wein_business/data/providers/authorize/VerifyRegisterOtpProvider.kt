package com.wein_business.data.providers.authorize

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.request.authorize.VerifyOtpRequest
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.authorize.UserModel
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class VerifyRegisterOtpProvider(var listener: Listener) {
    interface Listener {
        fun onVerifyOtpSuccess(userModel: UserModel)
        fun onVerifyOtpFail(message: String)
    }

    fun verifyRegisterOTP(verifyOtpRequest: VerifyOtpRequest) {
        val apiCall = RestClient.getApi().verifyRegisterOTP(verifyOtpRequest)

        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<UserModel>> {
            override fun onDataSuccess(body: AppResponse<UserModel>) {
                listener.onVerifyOtpSuccess(body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onVerifyOtpFail(Message)
            }
        }))
    }
}