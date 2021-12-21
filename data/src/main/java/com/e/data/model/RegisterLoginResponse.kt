package com.e.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class RegisterLoginResponse(

    @Json(name = "access_token")
    var accessToken: String,

    @Json(name = "refresh_token")
    var refreshToken: String,


    )