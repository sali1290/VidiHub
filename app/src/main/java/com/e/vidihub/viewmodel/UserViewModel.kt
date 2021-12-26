package com.e.vidihub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.domain.model.UserModel
import com.e.domain.usecase.GetUserInfoUseCase
import com.e.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val getUserUseCase: GetUserInfoUseCase) :
    ViewModel() {

    private val _user = MutableLiveData<Result<UserModel>>()
    val user: LiveData<Result<UserModel>>
        get() = _user

    private val handler = CoroutineExceptionHandler { _, exception ->
        _user.postValue(Result.Error(exception.message!!))
    }

    fun getUser() = viewModelScope.launch(Dispatchers.IO + handler) {
        _user.postValue(Result.Loading)
        getUserUseCase.execute().let {
            _user.postValue(Result.Success(it))
        }
    }
}