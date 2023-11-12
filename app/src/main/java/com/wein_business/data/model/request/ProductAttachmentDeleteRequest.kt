package com.wein_business.data.model.request

import com.google.gson.annotations.SerializedName

data class ProductAttachmentDeleteRequest(
    @SerializedName("activityId")
    var activityId: Int,
    @SerializedName("fileId")
    var fileId: String
)