package com.e.domain.usecase

import com.e.domain.repo.GetVideosRepo
import javax.inject.Inject

class GetSearchedVideoUseCase @Inject constructor(
    private val videosRepo: GetVideosRepo
) {

    suspend fun execute(category: String) = videosRepo.getSearchedVideos(category)

}