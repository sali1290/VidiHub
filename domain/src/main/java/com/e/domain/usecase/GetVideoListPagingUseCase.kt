package com.e.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.e.domain.model.VideoListItemModel
import com.e.domain.repo.GetVideosRepo
import javax.inject.Inject

class GetVideoListPagingUseCase @Inject constructor(private val getVideosRepo: GetVideosRepo) {

    suspend fun execute(): LiveData<PagingData<VideoListItemModel>> {
        return getVideosRepo.getVideoListPaging()
    }
}