package com.e.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(

    @Json(name = "userid")
    var userid: String?,

    @Json(name = "domain")
    var domain: String?,

    @Json(name = "account_type")
    var accountType: String?,

    @Json(name = "firstname")
    var firstName: String?,

    @Json(name = "lastname")
    var lastName: String?,

    @Json(name = "email")
    var email: String?,

    @Json(name = "phone")
    var phone: String?,

    @Json(name = "national_code")
    var nationalCode: String?,

    @Json(name = "plain_secret")
    var password: String?
)
