package com.e.vidihub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.domain.model.VideoListItemModel
import com.e.domain.model.VideoResponseModel
import com.e.domain.usecase.GetAllVideosUseCase
import com.e.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetAllVideosViewModel @Inject constructor(private val getAllVideosUseCase: GetAllVideosUseCase) :
    ViewModel() {

    private val _videos = MutableLiveData<Result<MutableList<VideoListItemModel>>>()
    val videos: LiveData<Result<MutableList<VideoListItemModel>>>
        get() = _videos

    private val handler = CoroutineExceptionHandler { _, exception ->
        _videos.postValue(Result.Error(exception.message!!))
    }

    fun getAllVideos() = viewModelScope.launch(Dispatchers.IO + handler) {
        _videos.postValue(Result.Loading)
        getAllVideosUseCase.execute().let {
            _videos.postValue(Result.Success(it))
        }
    }


}