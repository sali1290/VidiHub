package com.e.data.mapper

import com.e.data.model.VideoResponse
import com.e.domain.model.VideoResponseModel
import javax.inject.Inject

class VideoMapper @Inject constructor() {

    fun toMapper(videoResponse: VideoResponse): VideoResponseModel {

        return VideoResponseModel(
            videoResponse.src ?: "",
            videoResponse.poster ?: "",
            videoResponse.sub
        )

    }

}