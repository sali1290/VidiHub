package com.e.vidihub.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.e.data.model.VideoListItem
import com.e.data.repo.GetVideosRepoImpl
import com.e.domain.model.VideoListItemModel
import com.e.domain.usecase.GetVideoListPagingUseCase
import com.e.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetVideoPagingListViewModel @Inject constructor(private val getVideoListPagingUseCase: GetVideoListPagingUseCase) :
    ViewModel() {


//    private val _videos = MutableLiveData<Result<VideoListItemModel>>()
//    val videos: LiveData<Result<VideoListItemModel>>
//        get() = _videos
//
//    private val handler = CoroutineExceptionHandler { _, exception ->
//        _videos.postValue(Result.Error(exception.message!!))
//    }

    suspend fun fetchVideosLiveData(): LiveData<PagingData<VideoListItemModel>> {
        return getVideoListPagingUseCase.execute()
            .cachedIn(viewModelScope)
            .map { it.map { VideoListItemModel(it.vid, it.title, it.duration, it.guid) } }

    }


}