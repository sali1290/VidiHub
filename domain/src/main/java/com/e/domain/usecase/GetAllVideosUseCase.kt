package com.e.domain.usecase

import com.e.domain.repo.GetVideosRepo
import javax.inject.Inject

class GetAllVideosUseCase @Inject constructor(private val getVideosRepo: GetVideosRepo) {

    suspend fun execute() = getVideosRepo.getAllVideos()

}