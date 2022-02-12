package com.e.domain.usecase

import com.e.domain.repo.UserRepo
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val userRepo: UserRepo) {

    suspend fun execute() = userRepo.getUserInfo()

}