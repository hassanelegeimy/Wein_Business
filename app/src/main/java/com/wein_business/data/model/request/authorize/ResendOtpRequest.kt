package com.wein_business.data.model.request.authorize

import com.google.gson.annotations.SerializedName

data class ResendOtpRequest(
    @SerializedName("otpId")
    var otpId: String
)