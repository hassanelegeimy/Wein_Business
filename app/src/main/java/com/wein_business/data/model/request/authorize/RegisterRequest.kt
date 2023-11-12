package com.wein_business.data.model.request.authorize

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("userType")
    var userType: String,
    @SerializedName("genderType")
    var genderType: String,
    @SerializedName("userName")
    var userName: String,
    @SerializedName("mobileNumber")
    var mobileNumber: String,
    @SerializedName("password")
    var password: String
)