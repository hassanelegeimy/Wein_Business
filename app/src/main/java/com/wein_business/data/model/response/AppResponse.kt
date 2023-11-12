package com.wein_business.data.model.response

import com.google.gson.annotations.SerializedName

class AppResponse<T>(
    @SerializedName("data")
    var data: T
) : GenericResponse()
