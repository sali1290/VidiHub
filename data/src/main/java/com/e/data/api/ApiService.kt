package com.e.data.api

import com.e.data.model.Login
import com.e.data.model.RegisterLoginResponse
import com.e.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("accounts/useradd/")
    suspend fun register(@Body user: User): String

    @POST("accounts/login/")
    suspend fun login(@Body login: Login): Response<RegisterLoginResponse>


}