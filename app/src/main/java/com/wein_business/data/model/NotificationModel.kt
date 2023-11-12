package com.wein_business.data.model

import com.google.gson.annotations.SerializedName

data class NotificationModel(
    @SerializedName("idProduct")
    var idProduct: Int,
    @SerializedName("id")
    var id: Int,
)