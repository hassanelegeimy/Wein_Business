package com.wein_business.data.model.request

import com.google.gson.annotations.SerializedName

data class TripPriceRequest(
    @SerializedName("priceClassId")
    var priceClassId: Int,
    @SerializedName("providerPrice")
    var providerPrice: Int,
    @SerializedName("numberOfSeats")
    var numberOfSeats: Int
)