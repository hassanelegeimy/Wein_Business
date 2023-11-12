package com.wein_business.data.model.profile

import com.google.gson.annotations.SerializedName
import com.wein_business.data.model.ProviderStatusModel

data class IndividualProfileModel(
    @SerializedName("id") var id: Int,
    @SerializedName("individualName") var individualName: String,
    @SerializedName("idNumber") var idNumber: String,
    @SerializedName("iban") var iban: String,
    @SerializedName("cityId") var cityId: Int,
    @SerializedName("mobile") var mobile: String,
    @SerializedName("email") var email: String,
    @SerializedName("attachments") var attachments: ArrayList<ProfileAttachmentModel>,
    @SerializedName("completedPercent") var completedPercent: Int,
    @SerializedName("userStatus") var userStatus: ProviderStatusModel,
)