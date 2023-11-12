package com.wein_business.data.model.response

import com.google.gson.annotations.SerializedName

class AppResponseList<T>(
    @SerializedName("data")
    var data: ArrayList<T>
) : GenericResponse()

