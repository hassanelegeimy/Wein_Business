package com.wein_business.data.model

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("fileId")
    var fileId: String,
    @SerializedName("categoryLookup")
    var categoryLookup: LookupModel,
)