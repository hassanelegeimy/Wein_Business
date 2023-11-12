package com.wein_business.data.model.trip

import com.google.gson.annotations.SerializedName
import com.wein_business.data.model.LookupModel

data class ProviderTripPriceModel(
    @SerializedName("priceClass")
    var priceClass: LookupModel,
    @SerializedName("price")
    var price: Int,
    @SerializedName("numberOfSeats")
    var numberOfSeats: Int,
    @SerializedName("numberOfReservedSeats")
    var numberOfReservedSeats: Int
)