package com.wein_business.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileCompanyRequest(
    @SerializedName("companyName")
    var companyName: String,
    @SerializedName("crNumber")
    var crNumber: String,
    @SerializedName("iban")
    var iban: String ,
    @SerializedName("cityId")
    var cityId: Int,
    @SerializedName("email")
    var email: String
)