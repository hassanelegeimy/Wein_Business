package com.wein_business.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileIndividualRequest(
    @SerializedName("individualName")
    var individualName: String,
    @SerializedName("idNumber")
    var idNumber: String,
    @SerializedName("iban")
    var iban: String ,
    @SerializedName("cityId")
    var cityId: Int,
    @SerializedName("email")
    var email: String
)