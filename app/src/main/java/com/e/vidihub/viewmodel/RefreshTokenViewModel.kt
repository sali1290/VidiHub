package com.e.vidihub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.domain.usecase.RefreshTokenUseCase
import com.e.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefreshTokenViewModel @Inject constructor(private val refreshTokenUseCase: RefreshTokenUseCase) :
    ViewModel() {

    private val _token = MutableLiveData<Result<String>>()
    val token: LiveData<Result<String>>
        get() = _token

    private val handler = CoroutineExceptionHandler { _, exception ->
        _token.postValue(Result.Error(exception.message!!))
    }

    fun refreshToken(token: String) = viewModelScope.launch(Dispatchers.IO + handler) {
        _token.postValue(Result.Loading)
        refreshTokenUseCase.execute(token).let {
            _token.postValue(Result.Success(it))
        }
    }


}