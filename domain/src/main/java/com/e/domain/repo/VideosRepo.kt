package com.e.domain.repo

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.e.domain.model.CategoryResponseModel
import com.e.domain.model.VideoListItemModel
import com.e.domain.model.VideoResponseModel

interface VideosRepo {

    suspend fun getAllVideos(): MutableList<VideoListItemModel>

    suspend fun getVideo(vid: String): VideoResponseModel

    suspend fun getVideoListPaging(): LiveData<PagingData<VideoListItemModel>>

    suspend fun getCategories(): MutableList<CategoryResponseModel>

    suspend fun getSearchedVideosWithCategory(category: String): LiveData<PagingData<VideoListItemModel>>

    suspend fun getSearchedVideosWithName(videoName: String): LiveData<PagingData<VideoListItemModel>>
}