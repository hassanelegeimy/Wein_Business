package com.wein_business.data.model.profile

import com.google.gson.annotations.SerializedName
import com.wein_business.utils.Constants
import com.wein_business.utils.SessionManager

data class ProfileAttachmentModel(
    @SerializedName("fileId") var fileId: String,
    @SerializedName("mimeType") var mimeType: String,
    @SerializedName("fileName") var fileName: String,
    @SerializedName("type") var type: String,
    @SerializedName("nameAr") var nameAr: String,
    @SerializedName("nameEn") var nameEn: String

) {
    override fun toString(): String {
        return if (SessionManager.sessionLang
            == Constants.LOCALE_ARABIC && nameAr != null && nameAr.isNotEmpty()
        ) {
            nameAr
        } else {
            nameEn
        }
    }

}