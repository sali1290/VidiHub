package com.e.data.repo

import android.util.Log
import com.e.data.api.ApiService
import com.e.data.mapper.LoginMapper
import com.e.data.util.NetWorkHelper
import com.e.data.util.SessionManager
import com.e.domain.model.LoginModel
import com.e.domain.model.RegisterLoginResponseModel
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


    override suspend fun login(loginModel: LoginModel): RegisterLoginResponseModel {


        if (netWorkHelper.isNetworkConnected()) {
            val request = apiService.login(loginMapper.toLogin(loginModel))
            when (request.code()) {
                200 -> {
                    return RegisterLoginResponseModel(
                        request.body()!!.accessToken,
                        request.body()!!.refreshToken
                    )
                }

                else -> {
//                    Log.i("error", request.errorBody()!!.string())
                    throw IOException(request.errorBody()!!.string())
                }

            }
        } else {
            throw IOException("لطفا اتصال اینترنت خود را بررسی کنید")
        }


    }

    override suspend fun register(userModel: UserModel): String {
        return "null"
    }

    override suspend fun refreshToken(token: String): String {


        if (netWorkHelper.isNetworkConnected()) {
            val request = apiService.refreshToken("Bearer $token")
            when (request.code()) {
                200 -> {
                    return request.body()!!.token
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