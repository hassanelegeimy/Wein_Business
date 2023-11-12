package com.wein_business.data.model.authorize

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("username")
    var username: String,
    @SerializedName("displayName")
    var displayName: String,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("genderType")
    var genderType: String,
    @SerializedName("userType")
    var userType: String,
    @SerializedName("token")
    var token: String,
    @SerializedName("refreshToken")
    var refreshToken: String,
) {
    constructor() : this(0, "", "", "", "", "", "", "", "")
}