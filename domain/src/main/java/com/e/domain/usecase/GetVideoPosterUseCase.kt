package com.e.domain.usecase

import com.e.domain.repo.VideosRepo
import javax.inject.Inject

class GetVideoPosterUseCase @Inject constructor(private val videosRepo: VideosRepo) {

    suspend fun execute() = videosRepo.getPosterVideo()

}