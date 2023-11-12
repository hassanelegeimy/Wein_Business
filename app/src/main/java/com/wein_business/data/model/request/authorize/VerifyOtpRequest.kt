package com.wein_business.data.model.request.authorize

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(
    @SerializedName("otp")
    var otp: String ,
    @SerializedName("otpID")
    var otpID: String,
    @SerializedName("password")
    var password: String,
)