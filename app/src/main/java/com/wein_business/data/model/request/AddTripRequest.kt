package com.wein_business.data.model.request

import com.google.gson.annotations.SerializedName

data class AddTripRequest(
    @SerializedName("activityId")
    var activityId: Int,
    @SerializedName("startDate")
    var startDate: String,
    @SerializedName("endDate")
    var endDate: String,
    @SerializedName("startTime")
    var startTime: String,
    @SerializedName("endTime")
    var endTime: String,
    @SerializedName("prices")
    var prices: ArrayList<TripPriceRequest>
)