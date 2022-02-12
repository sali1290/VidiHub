package com.e.domain.usecase

import com.e.domain.repo.VideosRepo
import javax.inject.Inject

class GetSearchedVideosWithCategoryUseCase @Inject constructor(
    private val videosRepo: VideosRepo
) {

    suspend fun execute(category: String) = videosRepo.getSearchedVideosWithCategory(category)

}