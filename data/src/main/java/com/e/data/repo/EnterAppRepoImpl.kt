package com.e.data.repo

import android.util.Log
import com.e.data.api.ApiService
import com.e.data.mapper.LoginMapper
import com.e.data.util.NetWorkHelper
import com.e.data.util.SessionManager
import com.e.domain.model.LoginModel
import com.e.domain.model.UserModel
import com.e.domain.repo.EnterAppRepo
import java.io.IOException
import javax.inject.Inject

class EnterAppRepoImpl @Inject constructor(
    var apiService: ApiService,
    var netWorkHelper: NetWorkHelper,
    var loginMapper: LoginMapper,
    var sessionManager: SessionManager
) : EnterAppRepo {

    @Throws(IOException::class)
    override suspend fun login(loginModel: LoginModel): String {

        val request = apiService.login(loginMapper.toLogin(loginModel))
        if (netWorkHelper.isNetworkConnected()) {

            when (request.code()) {
                200 -> {
                    return request.body()!!.accessToken
                }

                else -> {
                    Log.i("error" , "error code is ${request.code()}")
                    throw IOException("مشکلی پیش آمده...")
                }

            }
        } else {
            throw IOException("لطفا اتصال اینترنت خود را بررسی کنید")
        }


    }

    override suspend fun register(userModel: UserModel): String {
        return "null"
    }
}