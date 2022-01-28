package com.e.vidihub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.e.domain.model.VideoListItemModel
import com.e.domain.usecase.GetSearchedVideoUseCase
import com.e.domain.usecase.GetVideoListPagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoPagingViewModel @Inject constructor(
    private val getVideoListPagingUseCase: GetVideoListPagingUseCase,
    private val getSearchedVideoUseCase: GetSearchedVideoUseCase
) :
    ViewModel() {

    suspend fun fetchVideosLiveData(): LiveData<PagingData<VideoListItemModel>> {
        return getVideoListPagingUseCase.execute()
            .cachedIn(viewModelScope)
            .map {
                it.map {
                    VideoListItemModel(
                        it.vid,
                        it.title,
                        it.duration,
                        it.thumbnail,
                        it.guid
                    )
                }
            }
    }

    suspend fun fetchSearchedVideosLiveData(category: String): LiveData<PagingData<VideoListItemModel>> {
        return getSearchedVideoUseCase.execute(category)
            .cachedIn(viewModelScope)
            .map {
                it.map {
                    VideoListItemModel(
                        it.vid,
                        it.title,
                        it.duration,
                        it.thumbnail,
                        it.guid
                    )
                }
            }
    }


}