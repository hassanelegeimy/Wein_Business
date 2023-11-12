package com.wein_business.data.model.trip

import com.google.gson.annotations.SerializedName
import com.wein_business.data.model.LookupModel

data class ProviderTripModel(
    @SerializedName("title")
    var title: String,
    @SerializedName("imageMaster")
    var imageMaster: String,
    @SerializedName("activityId")//TOD Review
    var activityId: Int,
    @SerializedName("startDate")
    var startDate: String,
    @SerializedName("endDate")
    var endDate: String,
    @SerializedName("startTime")
    var startTime: String,
    @SerializedName("endTime")
    var endTime: String,
    @SerializedName("tripStatus")
    var tripStatus: LookupModel,
    @SerializedName("prices")
    var prices: ArrayList<ProviderTripPriceModel>
)