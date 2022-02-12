package com.e.domain.repo

import com.e.domain.model.UserModel

interface UserRepo {

    suspend fun getAllUsers(): MutableList<UserModel>

    suspend fun getUserInfo(): UserModel


}