package com.wein_business.data.model.product

import com.google.gson.annotations.SerializedName

data class ProductAttachmentModel( // only in upload response
    @SerializedName("fileId") var fileId: String,
    @SerializedName("type") var type: String,//IMAGE_MASTER , IMAGE , VIDEO
    //Only for Display
    @SerializedName("displayName") var displayName: String

//    @SerializedName("fileName") var fileName: String,
//    @SerializedName("mimeType") var mimeType: String,
//    @SerializedName("nameAr") var nameAr: String,
//    @SerializedName("nameEn") var nameEn: String\
)