package com.e.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoPoster(

    @Json(name = "title")
    val title: String?,

    @Json(name = "poster_link")
    val posterLink: String?,

    @Json(name = "guid")
    val videoId: String?,


)
