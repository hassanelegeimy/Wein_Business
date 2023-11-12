package com.wein_business.data.model.product

import com.google.gson.annotations.SerializedName
import com.wein_business.data.model.LookupModel
import com.wein_business.data.model.ProductStatusModel

data class ProviderProductModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("category")
    var category: LookupModel,
    @SerializedName("city")
    var city: LookupModel,
    @SerializedName("status")
    var status: ProductStatusModel,
    @SerializedName("latitude")
    var latitude: Double,
    @SerializedName("longitude")
    var longitude: Double,
    @SerializedName("description")
    var description: String,
    @SerializedName("imageMaster")
    var imageMaster: String,
    @SerializedName("images")
    var images: ArrayList<String>,
    @SerializedName("videos")
    var videos: ArrayList<String>,
    )