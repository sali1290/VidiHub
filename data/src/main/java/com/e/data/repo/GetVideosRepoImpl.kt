package com.e.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.*
import com.e.data.api.ApiService
import com.e.data.mapper.VideoListItemMapper
import com.e.data.mapper.VideoMapper
import com.e.data.model.VideoListItem
import com.e.data.util.NetWorkHelper
import com.e.data.videoDatasource.VideoPagingSource
import com.e.domain.model.VideoListItemModel
import com.e.domain.model.VideoResponseModel
import com.e.domain.repo.GetVideosRepo
import java.io.IOException
import javax.inject.Inject

class GetVideosRepoImpl @Inject constructor(
    private val netWorkHelper: NetWorkHelper,
    private val apiService: ApiService,
    private val videoListItemMapper: VideoListItemMapper,
    private val videoResponseMapper: VideoMapper
) : GetVideosRepo {
    override suspend fun getAllVideos(): MutableList<VideoListItemModel> {


        if (netWorkHelper.isNetworkConnected()) {
            val request = apiService.getAllVideos()
            when (request.code()) {
                200 -> {
                    val list = request.body()!!.map {
                        videoListItemMapper.toMapper(it)
                    }.toMutableList()
                    return list

                }

                else -> {
                    Log.i("error", request.errorBody()!!.string())
                    throw IOException("مشکلی پیش آمده...")
                }

            }
        } else {
            throw IOException("لطفا اتصال اینترنت خود را بررسی کنید")
        }
    }

    override suspend fun getVideo(vid: String): VideoResponseModel {


        if (netWorkHelper.isNetworkConnected()) {
            val request = apiService.getVideo(vid)
            when (request.code()) {
                200 -> {
                    return videoResponseMapper.toMapper(request.body()!!)
                }

                else -> {
                    Log.i("error", request.errorBody()!!.string())
                    throw IOException("مشکلی پیش آمده...")
                }

            }
        } else {
            throw IOException("لطفا اتصال اینترنت خود را بررسی کنید")
        }
    }

    override suspend fun getVideoListPaging(): LiveData<PagingData<VideoListItemModel>> {
        return Pager(
            config = PagingConfig(1, enablePlaceholders = false),
            pagingSourceFactory = { VideoPagingSource(apiService) }
        ).liveData.map {
            it.map { VideoListItemModel(it.vid, it.title, it.duration, it.thumbnail, it.guid) }
        }

    }

}