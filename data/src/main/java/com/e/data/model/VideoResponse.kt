package com.e.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoResponse(

    @Json(name = "src")
    val src: String?,

    @Json(name = "poster")
    val poster: String?,

    @Json(name = "sub")
    val sub: MutableList<String>


)
