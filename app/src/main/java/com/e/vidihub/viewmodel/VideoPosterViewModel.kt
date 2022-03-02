package com.e.vidihub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.domain.model.VideoPosterModel
import com.e.domain.usecase.GetVideoPosterUseCase
import com.e.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPosterViewModel @Inject constructor(private val getVideoPosterUseCase: GetVideoPosterUseCase) :
    ViewModel() {

    private val _videos = MutableLiveData<Result<MutableList<VideoPosterModel>>>()
    val videos: LiveData<Result<MutableList<VideoPosterModel>>>
        get() = _videos

    private val handler = CoroutineExceptionHandler { _, exception ->
        _videos.postValue(Result.Error(exception.message!!))
    }

    fun getVideoPosters() = viewModelScope.launch(Dispatchers.IO + handler) {
        _videos.postValue(Result.Loading)
        getVideoPosterUseCase.execute().let {
            _videos.postValue(Result.Success(it))
        }

    }


}