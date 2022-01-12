package com.e.domain.usecase

import com.e.domain.repo.EnterAppRepo
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(private val enterAppRepo: EnterAppRepo) {

    suspend fun execute(token: String) = enterAppRepo.refreshToken(token)
}