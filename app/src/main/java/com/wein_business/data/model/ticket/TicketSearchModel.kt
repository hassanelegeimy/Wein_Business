package com.wein_business.data.model.ticket

import com.google.gson.annotations.SerializedName
import com.wein_business.data.model.LookupModel

data class TicketSearchModel(
    @SerializedName("title")
    var title: String,
    @SerializedName("imageMaster")
    var imageMaster: String,
    @SerializedName("startDate")
    var startDate: String,
    @SerializedName("endDate")
    var endDate: String,
    @SerializedName("startTime")
    var startTime: String,
    @SerializedName("endTime")
    var endTime: String,
    @SerializedName("statusClass")
    var statusClass: LookupModel,
    @SerializedName("priceClasses")
    var priceClasses: ArrayList<TicketSearchPriceModel>
)