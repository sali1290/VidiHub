package com.e.vidihub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.domain.model.VideoResponseModel
import com.e.domain.usecase.GetVideoUseCase
import com.e.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetVideoViewModel @Inject constructor(private val getVideoUseCase: GetVideoUseCase) :
    ViewModel() {

    private val _video = MutableLiveData<Result<VideoResponseModel>>()
    val video: LiveData<Result<VideoResponseModel>>
        get() = _video

    private val handler = CoroutineExceptionHandler { _, exception ->
        _video.postValue(Result.Error(exception.message!!))
    }

    fun playVideo(vid: String) = viewModelScope.launch(Dispatchers.IO + handler) {
        _video.postValue(Result.Loading)
        getVideoUseCase.execute(vid).let {
            _video.postValue(Result.Success(it))
        }
    }


}