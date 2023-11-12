package com.wein_business.data.providers.callback

import com.wein_business.R
import com.wein_business.data.model.response.GenericResponse
import com.wein_business.utils.NetworkUtils
import com.wein_business.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppCallback<T>(listener: AppCallbackListener<T>) : Callback<T> {
    var listener: AppCallbackListener<T>

    init {
        this.listener = listener
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (NetworkUtils.isSuccessResponse(response)) {
            val genericResponseModel = response.body() as GenericResponse
            if (genericResponseModel.isSuccess()) {
                listener.onDataSuccess(response.body()!!)
            } else {
                listener.onDataFail(genericResponseModel.message)
            }
        } else {
            listener.onDataFail(NetworkUtils.getApiErrorMessage(response));
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        listener.onDataFail(Utility.getString(R.string.error_api_msg))
    }
}