package com.wein_business.data.model

import com.google.gson.annotations.SerializedName

data class ProviderStatusModel(
    @SerializedName("userStatusLookup")
    var userStatusLookup: LookupModel,
    @SerializedName("userStatusEnum")
    var userStatusEnum: String,
    @SerializedName("adminComment")
    var adminComment: String,
    @SerializedName("showComment")
    var showComment: Boolean,
) {
    constructor() : this(LookupModel(), "", "", false)
}