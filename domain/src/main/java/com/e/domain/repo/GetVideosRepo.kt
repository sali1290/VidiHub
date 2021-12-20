package com.e.domain.repo

interface GetVideosRepo {

    suspend fun getAllVideos(): MutableList<String>

    suspend fun getVideo(id: String): String


}