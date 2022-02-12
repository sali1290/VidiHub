package com.e.data.repo

import android.util.Log
import com.e.data.api.ApiService
import com.e.data.mapper.UserMapper
import com.e.data.util.NetWorkHelper
import com.e.domain.model.UserModel
import com.e.domain.repo.UserRepo
import java.io.IOException
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val netWorkHelper: NetWorkHelper,
    private val userMapper: UserMapper
) : UserRepo {
    override suspend fun getAllUsers(): MutableList<UserModel> {
        return null!!
    }


    override suspend fun getUserInfo(): UserModel {

        if (netWorkHelper.isNetworkConnected()) {
            val request = apiService.getUser()
            when (request.code()) {
                200 -> {
                    return userMapper.toMapper(request.body()!!)
                }

                else -> {
                    Log.i("error", request.errorBody()!!.string())
                    throw IOException("مشکلی پیش آمده..." + request.code())
                }
            }

        } else {
            throw IOException("لطفا اتصال اینترنت خود را بررسی کنید")
        }
    }
}