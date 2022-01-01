package com.e.data.mapper

import com.e.data.model.VideoListItem
import com.e.domain.model.VideoListItemModel
import javax.inject.Inject

class VideoListItemMapper @Inject constructor() {

    fun toMapper(videoListItem: VideoListItem): VideoListItemModel {
        return VideoListItemModel(
            videoListItem.vid ?: "",
            videoListItem.title ?: "",
            videoListItem.duration ?: "",
            videoListItem.guid ?: "",
        )
    }

}