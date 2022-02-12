package com.e.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.e.domain.model.VideoListItemModel
import com.e.domain.repo.VideosRepo
import javax.inject.Inject

class GetVideoListPagingUseCase @Inject constructor(private val videosRepo: VideosRepo) {

    suspend fun execute(): LiveData<PagingData<VideoListItemModel>> {
        return videosRepo.getVideoListPaging()
    }
}