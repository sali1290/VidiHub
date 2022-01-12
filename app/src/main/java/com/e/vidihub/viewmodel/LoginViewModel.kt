package com.e.vidihub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.domain.model.LoginModel
import com.e.domain.model.RegisterLoginResponseModel
import com.e.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import com.e.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private var loginUseCase: LoginUseCase) : ViewModel() {

    private val _loginResponse = MutableLiveData<Result<RegisterLoginResponseModel>>()
    val loginResponse: LiveData<Result<RegisterLoginResponseModel>>
        get() = _loginResponse

    private val handler = CoroutineExceptionHandler { _, exception ->
        _loginResponse.postValue(exception.message!!.let {
            Result.Error(it)
        })
    }

    fun login(email: String, password: String) = viewModelScope.launch(Dispatchers.IO + handler) {
        _loginResponse.postValue(Result.Loading)
        loginUseCase.execute(LoginModel(email, password)).let {
            _loginResponse.postValue(Result.Success(it))
        }
    }


}