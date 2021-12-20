package com.e.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Login(

    @Json(name = "email")
    var email: String?,

    @Json(name = "plain_secret")
    var password: String?,


)
