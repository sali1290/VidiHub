package com.e.data.mapper

import com.e.data.model.VideoPoster
import com.e.domain.model.VideoPosterModel
import javax.inject.Inject

class VideoPosterMapper @Inject constructor() {

    fun toMapper(videoPoster: VideoPoster): VideoPosterModel {

        return VideoPosterModel(
            videoPoster.title ?: "",
            videoPoster.posterLink ?: "",
            videoPoster.videoId ?: ""
        )

    }

}