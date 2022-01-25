package com.e.data.repo

import android.util.Log
import com.e.data.api.ApiService
import com.e.data.mapper.DomainResponseMapper
import com.e.data.util.NetWorkHelper
import com.e.domain.model.DomainResponseModel
import com.e.domain.repo.GetDomainRepo
import java.io.IOException
import javax.inject.Inject

class GetDomainRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val netWorkHelper: NetWorkHelper,
    private val domainResponseMapper: DomainResponseMapper
) : GetDomainRepo {
    override suspend fun getAllDomains(): MutableList<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getDomainInfo(): DomainResponseModel {
        if (netWorkHelper.isNetworkConnected()) {
            val request = apiService.getDomain()
            when (request.code()) {
                200 -> {
                    return domainResponseMapper.toMapper(request.body()!!)
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