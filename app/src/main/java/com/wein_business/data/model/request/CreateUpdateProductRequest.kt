package com.wein_business.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateUpdateProductRequest(
    //for Update
    @SerializedName("id")
    var id: Int ,

    //for Creation
    @SerializedName("title")
    var title: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("activityCategoryId")
    var activityCategoryId: Int,
    @SerializedName("cityId")
    var cityId: Int,
    @SerializedName("latitude")
    var latitude: Double,
    @SerializedName("longitude")
    var longitude: Double
)