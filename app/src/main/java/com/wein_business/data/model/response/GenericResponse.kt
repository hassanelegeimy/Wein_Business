package com.wein_business.data.model.response

import com.google.gson.annotations.SerializedName

open class GenericResponse{
    @SerializedName("code")
    var code :String = ""
    @SerializedName("message")
    var message: String = "test"

    fun isSuccess(): Boolean {
        return code == "200"
    }
}