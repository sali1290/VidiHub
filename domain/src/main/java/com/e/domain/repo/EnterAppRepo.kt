package com.e.domain.repo

import com.e.domain.model.LoginModel
import com.e.domain.model.UserModel

interface EnterAppRepo {

    suspend fun login(loginModel: LoginModel): String

    suspend fun register(userModel: UserModel): String


}