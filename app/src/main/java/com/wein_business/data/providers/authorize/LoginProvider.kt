package com.wein_business.data.providers.authorize

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.request.authorize.LoginRequest
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.authorize.UserModel
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class LoginProvider(var listener: Listener) {
    interface Listener {
        fun onLoginSuccess(userModel: UserModel)
        fun onLoginFail(message: String)
    }

    fun login(loginRequest: LoginRequest) {
        val apiCall = RestClient.getApi().login(loginRequest)

        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<UserModel>> {
            override fun onDataSuccess(body: AppResponse<UserModel>) {
                listener.onLoginSuccess(body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onLoginFail(Message)
            }
        }))
    }
}