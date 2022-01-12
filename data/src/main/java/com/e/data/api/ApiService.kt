package com.e.data.api

import com.e.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("accounts/useradd/")
    suspend fun register(@Body user: User): String

    @POST("accounts/login/")
    suspend fun login(@Body login: Login): Response<RegisterLoginResponse>

    @GET("accounts/userinfo/")
    suspend fun getUser(): Response<User>

    @GET("videos/videolist/")
    suspend fun getAllVideos(): Response<MutableList<VideoListItem>>


    @GET("videos/videoplaybackinfo/{video_id}")
    suspend fun getVideo(
        @Path("video_id") video_id: String
    ): Response<VideoResponse>

    @GET("accounts/refresh_token/")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): Response<String>
}