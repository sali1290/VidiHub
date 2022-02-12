package com.e.domain.usecase

import com.e.domain.repo.VideosRepo
import javax.inject.Inject

class GetVideoUseCase @Inject constructor(private val videosRepo: VideosRepo) {

    suspend fun execute(vid: String) = videosRepo.getVideo(vid)
}