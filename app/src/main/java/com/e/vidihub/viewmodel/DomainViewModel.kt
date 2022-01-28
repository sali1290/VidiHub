package com.e.vidihub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.domain.model.DomainResponseModel
import com.e.domain.usecase.GetDomainUseCase
import com.e.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DomainViewModel @Inject constructor(
    private val getDomainUseCase: GetDomainUseCase
) : ViewModel() {

    private val _domainInfo = MutableLiveData<Result<DomainResponseModel>>()
    val domainInfo: LiveData<Result<DomainResponseModel>>
        get() = _domainInfo

    private val handler = CoroutineExceptionHandler { _, exception ->
        _domainInfo.postValue(Result.Error(exception.message!!))
    }

    fun getDomain() = viewModelScope.launch(Dispatchers.IO + handler) {
        _domainInfo.postValue(Result.Loading)
        getDomainUseCase.execute().let {
            _domainInfo.postValue(Result.Success(it))
        }
    }


}