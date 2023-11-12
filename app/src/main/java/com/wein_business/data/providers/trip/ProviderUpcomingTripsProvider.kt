package com.wein_business.data.providers.trip

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.trip.ProviderTripModel
import com.wein_business.data.model.response.AppResponseList
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class ProviderUpcomingTripsProvider(var listener: Listener) {
    interface Listener {
        fun onProviderUpcomingTripsSuccess(tripsList: ArrayList<ProviderTripModel>)
        fun onProviderUpcomingTripsFail(errorMessage: String?)
    }

    fun getProviderTrips() {
        val apiCall = RestClient.getApi().getProviderUpcomingTrips()
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponseList<ProviderTripModel>> {
            override fun onDataSuccess(body: AppResponseList<ProviderTripModel>) {
                listener.onProviderUpcomingTripsSuccess(body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onProviderUpcomingTripsFail(Message)
            }
        }))
    }
}