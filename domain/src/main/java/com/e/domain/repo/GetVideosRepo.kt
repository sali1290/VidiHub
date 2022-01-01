package com.e.domain.repo

import com.e.domain.model.VideoListItemModel
import com.e.domain.model.VideoResponseModel

interface GetVideosRepo {

    suspend fun getAllVideos(): MutableList<VideoListItemModel>

    suspend fun getVideo(vid: String): VideoResponseModel


}