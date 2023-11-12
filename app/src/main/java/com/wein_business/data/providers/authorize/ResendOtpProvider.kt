package com.wein_business.data.providers.authorize

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.request.authorize.ResendOtpRequest
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.authorize.OtpModel
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class ResendOtpProvider(var listener: Listener) {
    interface Listener {
        fun onResendOtpSuccess(otpModel : OtpModel, message:String)
        fun onResendOtpFail(message: String)
    }

    fun resendOTP(resendOtpRequest: ResendOtpRequest) {
        val apiCall = RestClient.getApi().resendOtp(resendOtpRequest)

        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<OtpModel>> {
            override fun onDataSuccess(body: AppResponse<OtpModel>) {
                listener.onResendOtpSuccess(body.data , body.message)
            }

            override fun onDataFail(Message: String) {
                listener.onResendOtpFail(Message)
            }
        }))
    }
}