package com.e.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.*
import com.e.data.api.ApiService
import com.e.data.mapper.CategoryMapper
import com.e.data.mapper.VideoListItemMapper
import com.e.data.mapper.VideoMapper
import com.e.data.mapper.VideoPosterMapper
import com.e.data.util.NetWorkHelper
import com.e.data.videoDatasource.VideoWithCategoryPagingSource
import com.e.data.videoDatasource.VideoPagingSource
import com.e.data.videoDatasource.VideoWithNamePagingSource
import com.e.domain.model.CategoryResponseModel
import com.e.domain.model.VideoListItemModel
import com.e.domain.model.VideoPosterModel
import com.e.domain.model.VideoResponseModel
import com.e.domain.repo.VideosRepo
import java.io.IOException
import javax.inject.Inject

class VideosRepoImpl @Inject constructor(
    private val netWorkHelper: NetWorkHelper,
    private val apiService: ApiService,
    private val videoListItemMapper: VideoListItemMapper,
    private val categoryMapper: CategoryMapper,
    private val videoResponseMapper: VideoMapper,
    private val videoPosterMapper: VideoPosterMapper
) : VideosRepo {
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

    override suspend fun getSearchedVideosWithCategory(category: String): LiveData<PagingData<VideoListItemModel>> {
        return Pager(
            config = PagingConfig(1, enablePlaceholders = false),
            pagingSourceFactory = { VideoWithCategoryPagingSource(apiService, category) }
        ).liveData.map {
            it.map { VideoListItemModel(it.vid, it.title, it.duration, it.thumbnail, it.guid) }
        }
    }

    override suspend fun getSearchedVideosWithName(videoName: String): LiveData<PagingData<VideoListItemModel>> {
        return Pager(
            config = PagingConfig(1, enablePlaceholders = false),
            pagingSourceFactory = { VideoWithNamePagingSource(apiService, videoName) }
        ).liveData.map {
            it.map { VideoListItemModel(it.vid, it.title, it.duration, it.thumbnail, it.guid) }
        }
    }

    override suspend fun getPosterVideo(): MutableList<VideoPosterModel> {
        if (netWorkHelper.isNetworkConnected()) {
            val request = apiService.getVideoPoster()
            when (request.code()) {
                200 -> {
                    return request.body()!!.map {
                        videoPosterMapper.toMapper(it)
                    }.toMutableList()
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


    override suspend fun getCategories(): MutableList<CategoryResponseModel> {
        if (netWorkHelper.isNetworkConnected()) {
            val request = apiService.getCategories()
            when (request.code()) {
                200 -> {
                    return request.body()!!.map {
                        categoryMapper.toMapper(it)
                    }.toMutableList()
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


}