package com.e.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryResponse(

    @Json(name = "cid")
    val cid: String?,

    @Json(name = "name")
    val name: String?,

    @Json(name = "parent")
    val parent: String?

)