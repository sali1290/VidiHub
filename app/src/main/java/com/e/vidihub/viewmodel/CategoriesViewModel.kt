package com.e.vidihub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.domain.model.CategoryResponseModel
import com.e.domain.usecase.GetCategoryUseCase
import com.e.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val getCategoryUseCase: GetCategoryUseCase) :
    ViewModel() {

    private val _categories = MutableLiveData<Result<MutableList<CategoryResponseModel>>>()
    val category: LiveData<Result<MutableList<CategoryResponseModel>>>
        get() = _categories

    private val handler = CoroutineExceptionHandler { _, exception ->
        _categories.postValue(Result.Error(exception.message!!))
    }

    fun getCategories() = viewModelScope.launch(Dispatchers.IO + handler) {
        _categories.postValue(Result.Loading)
        getCategoryUseCase.execute().let {
            _categories.postValue(Result.Success(it))
        }
    }

}