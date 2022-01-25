package com.e.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DomainResponse(

    @Json(name = "domain_id")
    val domainId: String?,

    @Json(name = "domain")
    val domain: String?,

    @Json(name = "hostname")
    val hostname: String?,

    @Json(name = "country")
    val country: String?,

    @Json(name = "comments")
    val comments: String?,

    @Json(name = "status")
    val status: String?,

    @Json(name = "domain_logo")
    val domainLogo: String?,
)
