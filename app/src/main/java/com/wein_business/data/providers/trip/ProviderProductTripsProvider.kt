package com.wein_business.data.providers.trip

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.trip.ProviderTripModel
import com.wein_business.data.model.response.AppResponseList
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class ProviderProductTripsProvider(var listener: Listener) {
    interface Listener {
        fun onProviderProductTripsSuccess(tripsList: ArrayList<ProviderTripModel>)
        fun onProviderProductTripsFail(errorMessage: String?)
    }

    fun getProviderProductTrips(activityId: Int) {
        val apiCall = RestClient.getApi().getActivityTrips(activityId)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponseList<ProviderTripModel>> {
            override fun onDataSuccess(body: AppResponseList<ProviderTripModel>) {
                listener.onProviderProductTripsSuccess(body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onProviderProductTripsFail(Message)
            }
        }))
    }
}