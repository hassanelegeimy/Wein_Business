package com.wein_business.data.model.profile

import com.google.gson.annotations.SerializedName
import com.wein_business.data.model.ProviderStatusModel

data class CompanyProfileModel(
    @SerializedName("id") var id: Int,
    @SerializedName("companyName") var companyName: String,
    @SerializedName("crNumber") var crNumber: String,
    @SerializedName("iban") var iban: String,
    @SerializedName("cityId") var cityId: Int,
    @SerializedName("mobile") var mobile: String,
    @SerializedName("email") var email: String,
    @SerializedName("attachments") var attachments: ArrayList<ProfileAttachmentModel>,
    @SerializedName("completedPercent") var completedPercent: Int,
    @SerializedName("userStatus") var userStatus: ProviderStatusModel
)
