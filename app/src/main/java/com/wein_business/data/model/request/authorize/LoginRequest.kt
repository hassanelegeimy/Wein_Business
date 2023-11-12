package com.wein_business.data.model.request.authorize

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("mobileOrEmail")
    var mobileOrEmail: String,
    @SerializedName("password")
    var password: String,
)