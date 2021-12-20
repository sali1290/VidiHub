package com.e.domain.repo

import com.e.domain.model.ChangePasswordModel
import com.e.domain.model.EditUserModel

interface ChangeUserInfoRepo {

    suspend fun changePassword(changePasswordModel: ChangePasswordModel): String

    suspend fun editUserInfo(editUserModel: EditUserModel):String

    suspend fun deleteUser(userId: String): String

}