package com.e.data.mapper

import com.e.data.model.User
import com.e.domain.model.UserModel
import javax.inject.Inject

class UserMapper @Inject constructor(){

    fun toMapper(user: User): UserModel {

        return UserModel(
            user.userid ?: "",
            user.domain ?: "",
            user.accountType ?: "",
            user.firstName ?: "",
            user.lastName ?: "",
            user.email ?: "",
            user.phone ?: "",
            user.nationalCode ?: "",
            user.password ?: "",
        )

    }

}