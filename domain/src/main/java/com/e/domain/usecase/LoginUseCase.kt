package com.e.domain.usecase

import com.e.domain.model.LoginModel
import com.e.domain.repo.EnterAppRepo
import javax.inject.Inject

class LoginUseCase @Inject constructor(private var enterAppRepo: EnterAppRepo){

    suspend fun execute(loginModel :LoginModel) = enterAppRepo.login(loginModel)

}