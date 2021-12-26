package com.e.domain.usecase

import com.e.domain.repo.GetUserRepo
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val getUserRepo: GetUserRepo) {

    suspend fun execute() = getUserRepo.getUserInfo()

}