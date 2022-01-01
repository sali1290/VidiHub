package com.e.domain.usecase

import com.e.domain.repo.GetVideosRepo
import javax.inject.Inject

class GetVideoUseCase @Inject constructor(private val getVideosRepo: GetVideosRepo) {

    suspend fun execute(vid: String) = getVideosRepo.getVideo(vid)
}