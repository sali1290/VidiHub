package com.e.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoListItem(

    @Json(name = "vid")
    val vid: String?,

    @Json(name = "title")
    val title: String?,

    @Json(name = "duration")
    val duration: String?,

    @Json(name = "guid")
    val guid: String?,

    )
