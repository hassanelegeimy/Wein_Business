package com.wein_business.data.providers.ticket

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.ticket.TicketSearchModel
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener

class TicketSearchProvider(var listener: Listener) {
    interface Listener {
        fun onTicketDetailsSuccess(bookingCode:String , ticketSearchModel: TicketSearchModel)
        fun onTicketDetailsFail(errorMessage: String)
    }

    fun searchTicket(bookingCode: String) {
        val apiCall = RestClient.getApi().searchTicket(bookingCode)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<TicketSearchModel>> {
            override fun onDataSuccess(body: AppResponse<TicketSearchModel>) {
                listener.onTicketDetailsSuccess(bookingCode , body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onTicketDetailsFail(Message)
            }
        }))
    }
}