package com.wein_business.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.gson.Gson
import com.wein_business.BaseApp
import com.wein_business.R
import com.wein_business.data.model.response.AppResponse
import com.wein_business.utils.Utility.getString
import retrofit2.Response

object NetworkUtils {
    fun isSuccessResponse(response: Response<*>): Boolean {
        return response.code() == 200 || response.code() == 201
    }

    fun getApiErrorMessage(response: Response<*>): String {
        //TODO enable to logout in unAuthorize
        if(response.code() == 401){
            SessionManager.logout(BaseApp.currentActivity)
            return  getString(R.string.session_expired)
        }
        //TODO HANDLE 404 CRASH

        val responseBody = response.errorBody()
        if (responseBody != null) {
            try {
                val g = Gson()
                val appResponse = g.fromJson(responseBody.string(), AppResponse::class.java)
                return appResponse.message
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return getString(R.string.error_api_msg) // default erro_api
    }

    //***************************************************************
    //****** Check Connection
    //TODO HASSAN
//    fun checkNetworkConnection(): Boolean {
//        val connectivityManager =
//            BaseApp.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetworkInfo = connectivityManager.activeNetworkInfo
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected
//    }

    fun isConnected() : Boolean{
        var result = false
        val connectivityManager =
            BaseApp.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }
}