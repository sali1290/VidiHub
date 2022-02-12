package com.e.domain.usecase

import com.e.domain.repo.VideosRepo
import javax.inject.Inject

class GetSearchedVideosWithNameUseCase @Inject constructor(private val videosRepo: VideosRepo) {

    suspend fun execute(videoName: String) = videosRepo.getSearchedVideosWithName(videoName)

}