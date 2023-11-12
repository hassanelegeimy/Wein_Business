package com.wein_business.data.api

import com.wein_business.data.api.RestClient.getApi
import com.wein_business.data.model.authorize.UserModel
import com.wein_business.data.model.request.authorize.RenewTokenRequest
import com.wein_business.utils.Constants
import com.wein_business.utils.SessionManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.util.concurrent.TimeUnit

object TokenAuthenticator : Authenticator {
    private var isRefreshingToken = false
    var retryCount = 0

    override fun authenticate(route: Route?, response: Response): Request? {
        retryCount++

        return if (!isRefreshingToken) {
            isRefreshingToken = true

                val refreshToken = SessionManager.userModel.refreshToken
                var refreshResponse =
                    getApi().renewToken(RenewTokenRequest(refreshToken)).execute().body()

                if (refreshResponse != null && refreshResponse.isSuccess()) {
                    var model:UserModel = refreshResponse.data
                    SessionManager.changeSessionUserModel(model)
                    retryCount = 0
                }
            isRefreshingToken = false
            retryRequest(response)
        } else {//is processing another api call
            TimeUnit.SECONDS.sleep(3)
            return retryRequest(response)
        }
    }

    private fun retryRequest(response: Response): Request? {
        if (retryCount > 3) {
            retryCount = 0
            return null
        }

        return response.request.newBuilder()
            .header(Constants.HEADER_AUTH, Constants.HEADER_BEARER + SessionManager.userModel.token)
            .build()
    }

}