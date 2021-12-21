package com.e.data.mapper

import com.e.data.model.Login
import com.e.domain.model.LoginModel
import javax.inject.Inject

class LoginMapper @Inject constructor() {

    fun toLogin(loginModel: LoginModel): Login {
        return Login(
            loginModel.email ?: "",
            loginModel.password ?: ""
        )
    }

}