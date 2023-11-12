package com.wein_business.data.model.request.authorize

import com.google.gson.annotations.SerializedName

data class RenewTokenRequest(
    @SerializedName("refreshToken")
    var refreshToken: String
)