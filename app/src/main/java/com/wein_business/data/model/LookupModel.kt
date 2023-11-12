package com.wein_business.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wein_business.utils.Constants
import com.wein_business.utils.SessionManager
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LookupModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("countryCode")
    var countryCode: String,
    @SerializedName("nameAr")
    var nameAr: String,
    @SerializedName("nameEn")
    var nameEn: String,
):Parcelable{

    constructor() : this(-1, "", "", "")

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