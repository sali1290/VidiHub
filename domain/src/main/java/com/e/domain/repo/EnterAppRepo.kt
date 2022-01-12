package com.e.domain.repo

import com.e.domain.model.LoginModel
import com.e.domain.model.RegisterLoginResponseModel
import com.e.domain.model.UserModel

interface EnterAppRepo {

    suspend fun login(loginModel: LoginModel): RegisterLoginResponseModel

    suspend fun register(userModel: UserModel): String

    suspend fun refreshToken(token: String): String


}