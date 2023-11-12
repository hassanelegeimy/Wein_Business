package com.wein_business.data.providers.callback

interface AppCallbackListener<T> {
    fun onDataSuccess(body: T)
    fun onDataFail(Message: String)
}