package com.wein_business.data.model

import com.google.gson.annotations.SerializedName

data class ProductStatusModel(
    @SerializedName("statusLookup")
    var statusLookup: LookupModel,
    @SerializedName("statusEnum")
    var statusEnum: String,
    @SerializedName("adminComment")
    var adminComment: String,
    @SerializedName("showComment")
    var showComment: Boolean,
)