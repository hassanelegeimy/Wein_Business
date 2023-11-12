package com.wein_business.data.model.ticket

import com.google.gson.annotations.SerializedName
import com.wein_business.data.model.LookupModel

data class TicketSearchPriceModel(
    @SerializedName("seatsCount")
    var seatsCount: Int,
    @SerializedName("priceClasses")
    var priceClasses: LookupModel
)