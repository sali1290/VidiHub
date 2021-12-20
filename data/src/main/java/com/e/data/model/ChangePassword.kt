package com.e.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChangePassword(

    @Json(name = "oldpassword")
    var oldPass: String?,

    @Json(name = "newpassword")
    var newPass: String?
)
